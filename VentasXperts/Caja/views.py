import datetime
import os
import io
from django.conf import settings
from django.contrib.auth.models import User, Group
from django.http import HttpResponse, FileResponse
from django.template.loader import render_to_string
from django.shortcuts import get_object_or_404
from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import action
from xhtml2pdf import pisa
from .permissions import IsCajero, IsAdministrador
from django.db import transaction
from Administracion.models import Producto, CarritoProducto, Carrito, Venta, Finanzas, Categoria, Proveedor, Caja, Persona
from .serializers import ProductoSerializer, CarritoProductoSerializer, VentaSerializer, FinanzasSerializer, CategoriaSerializer, ProveedorSerializer, CarritoSerializer, UserSerializer, PersonaSerializer, UserCreateSerializer, CajaSerializer

# Importar comandos para generar tickets
from .commands.listar_historial_command import ListarHistorialCommand
from .commands.descargar_ticket_command import DescargarTicketCommand
from .commands.generar_ticket_command import GenerarTicketCommand


class UserViewSet(viewsets.ViewSet):
    permission_classes = [IsAdministrador]
    @transaction.atomic
    @action(detail=False, methods=['post'])
    def create_user(self, request):
        """
        Crear un usuario junto con su persona asociada y asignarle un grupo existente por ID.
        crea un super usuario para poder hacer esto.
        """
        
        user_data = request.data.get('user')
        persona_data = request.data.get('persona')

        if not user_data or not persona_data:
            return Response({'detail': 'Datos del usuario o persona incompletos.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            user = User.objects.create_user(
                username=user_data['username'],
                email=user_data['email'],
                password=user_data['password'],
                first_name=user_data['first_name'],
                last_name=user_data['last_name']
            )
            user.save()

            # Asignar grupo existente por ID
            group_id = user_data.get('group_id')
            if group_id:
                try:
                    group = Group.objects.get(id=group_id)
                    user.groups.add(group)
                except Group.DoesNotExist:
                    return Response({'detail': f'Grupo con ID {group_id} no encontrado.'}, status=status.HTTP_404_NOT_FOUND)

            persona = Persona.objects.create(
                user=user,
                nombre=persona_data['nombre'],
                apPaterno=persona_data['apPaterno'],
                apMaterno=persona_data.get('apMaterno', ''),
                genero=persona_data['genero'],
                correo=persona_data['correo'],
                telefono=persona_data['telefono'],
                rfc=persona_data['rfc'],
                curp=persona_data['curp']
            )

            user_serializer = UserSerializer(user)
            persona_serializer = PersonaSerializer(persona)

            return Response({
                'user': user_serializer.data,
                'persona': persona_serializer.data
            }, status=status.HTTP_201_CREATED)

        except KeyError as e:
            return Response({'detail': f'Campo faltante: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

class CajaViewSet(viewsets.ModelViewSet):
    queryset = Caja.objects.all()
    serializer_class = CajaSerializer
    permission_classes = [IsCajero]

class ProductoViewSet(viewsets.ModelViewSet):
    queryset = Producto.objects.all()
    serializer_class = ProductoSerializer
    permission_classes = [IsCajero]
    
class CategoriaViewSet(viewsets.ModelViewSet):
    queryset = Categoria.objects.all()
    serializer_class = CategoriaSerializer
    permission_classes = [IsCajero]
    
class ProveedorViewSet(viewsets.ModelViewSet):
    queryset = Proveedor.objects.all()
    serializer_class = ProveedorSerializer
    permission_classes = [IsCajero]
    
class carritoViewSet(viewsets.ModelViewSet):
    queryset = Carrito.objects.all()
    serializer_class = CarritoSerializer
    permission_classes = [IsCajero]
    
class CarritoProductoViewSet(viewsets.ModelViewSet):
    queryset = CarritoProducto.objects.select_related('producto').all()
    serializer_class = CarritoProductoSerializer
    permission_classes = [IsCajero]

    @action(detail=True, methods=['post'])
    def agregar(self, request, pk=None):
        """Añade una unidad del producto al carrito"""
        producto = get_object_or_404(Producto, pk=pk)
        carrito_producto, created = CarritoProducto.objects.get_or_create(producto=producto)

        if carrito_producto.cantidad is None:
            carrito_producto.cantidad = 0  # Asignar un valor inicial
        
        if carrito_producto.subtotal is None:
            carrito_producto.subtotal = 0

        carrito_producto.cantidad += 1
        carrito_producto.subtotal += producto.precio_tienda
        carrito_producto.save()

        return Response({'message': 'Producto agregado', 'cantidad': carrito_producto.cantidad}, status=200)


    @action(detail=True, methods=['post'])
    def restar(self, request, pk=None):
        """Resta una unidad del producto en el carrito"""
        carrito_producto = get_object_or_404(CarritoProducto, producto_id=pk)
        if carrito_producto.cantidad > 0:
            carrito_producto.cantidad -= 1
            carrito_producto.subtotal -= carrito_producto.producto.precio_tienda
            carrito_producto.save()
            return Response({'message': 'Producto restado', 'cantidad': carrito_producto.cantidad}, status=status.HTTP_200_OK)
        return Response({'message': 'Cantidad ya es 0'}, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def quitar(self, request, pk=None):
        """Elimina el producto del carrito"""
        carrito_producto = get_object_or_404(CarritoProducto, producto_id=pk)
        carrito_producto.delete()
        return Response({'message': 'Producto eliminado del carrito'}, status=status.HTTP_200_OK)


class VentaViewSet(viewsets.ModelViewSet):
    queryset = Venta.objects.all()
    serializer_class = VentaSerializer
    permission_classes = [IsCajero]

    @action(detail=False, methods=['post'])
    def procesar_venta(self, request):
        """Procesa la venta, genera el ticket y vacía el carrito"""
        carrito_productos = CarritoProducto.objects.all()
        if not carrito_productos.exists():
            return Response({'message': 'El carrito está vacío'}, status=status.HTTP_400_BAD_REQUEST)

        total_costo = sum(cp.subtotal for cp in carrito_productos)
        fecha_actual = datetime.datetime.now()

        # Crear un registro en Finanzas
        finanzas = Finanzas.objects.create(fecha=fecha_actual.date(), hora=fecha_actual.time())

        # Funcion para comprobar el usuario en sesion y capturar su id
        user = User.objects.get(username=request.user)
        caja_actual = Caja.objects.get(user=user)

        # Crear la venta
        venta = Venta.objects.create(
            carrito=Carrito.objects.create(precio_total=total_costo),
            caja=caja_actual,  # Se asume que el usuario tiene una caja asociada
            finanzas=finanzas,
            total=total_costo,
            fecha=fecha_actual
        )

        # Vaciar carrito
        carrito_productos.delete()

        return Response({'message': 'Venta procesada', 'venta_id': venta.id, 'total': total_costo}, status=status.HTTP_201_CREATED)


class TicketViewSet(viewsets.ViewSet):
    permission_classes = [IsCajero]
    
    @action(detail=False, methods=['get'])
    def historial(self, request):
        return ListarHistorialCommand().execute(request)

    @action(detail=True, methods=['get'])
    def descargar(self, request, pk=None):
        return ListarHistorialCommand().execute(request)

    @action(detail=False, methods=['post'])
    def generar_ticket(self, request):
        return GenerarTicketCommand().execute(request)
