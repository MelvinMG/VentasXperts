import datetime
import os
import io
from django.conf import settings
from django.contrib.auth.models import User
from django.http import HttpResponse, FileResponse
from django.template.loader import render_to_string
from django.shortcuts import get_object_or_404
from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.decorators import action
from xhtml2pdf import pisa
from Administracion.models import Producto, CarritoProducto, Carrito, Venta, Finanzas, Categoria, Proveedor, Caja
from .serializers import ProductoSerializer, CarritoProductoSerializer, VentaSerializer, FinanzasSerializer, CategoriaSerializer, ProveedorSerializer, CarritoSerializer, UserCreateSerializer, CajaSerializer

class UserViewSet(viewsets.ViewSet):
    @action(detail=False, methods=['post'])
    def create_user(self, request):
        """Permite registrar un nuevo usuario"""
        serializer = UserCreateSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response({'message': 'Usuario creado exitosamente'}, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class CajaViewSet(viewsets.ModelViewSet):
    queryset = Caja.objects.all()
    serializer_class = CajaSerializer

class ProductoViewSet(viewsets.ModelViewSet):
    queryset = Producto.objects.all()
    serializer_class = ProductoSerializer
    
class CategoriaViewSet(viewsets.ModelViewSet):
    queryset = Categoria.objects.all()
    serializer_class = CategoriaSerializer
    
class ProveedorViewSet(viewsets.ModelViewSet):
    queryset = Proveedor.objects.all()
    serializer_class = ProveedorSerializer
    
class carritoViewSet(viewsets.ModelViewSet):
    queryset = Carrito.objects.all()
    serializer_class = CarritoSerializer
    
class CarritoProductoViewSet(viewsets.ModelViewSet):
    queryset = CarritoProducto.objects.select_related('producto').all()
    serializer_class = CarritoProductoSerializer

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
    @action(detail=False, methods=['get'])
    def historial(self, request):
        """Lista todos los tickets en PDF generados"""
        pdf_folder = os.path.join(settings.BASE_DIR, 'Caja', 'media', 'pdf_ticket')

        if not os.path.exists(pdf_folder):
            return Response({'message': 'No existe la carpeta'}, status=status.HTTP_404_NOT_FOUND)

        pdf_files = []
        for file_name in os.listdir(pdf_folder):
            if file_name.endswith('.pdf'):
                file_path = os.path.join(pdf_folder, file_name)
                creation_time = datetime.datetime.fromtimestamp(os.path.getctime(file_path))
                pdf_files.append({
                    'name': file_name,
                    'creation_time': creation_time.strftime("%Y-%m-%d %H:%M:%S"),
                    'path': f'{settings.MEDIA_URL}pdf_ticket/{file_name}'
                })

        pdf_files.sort(key=lambda x: x['creation_time'], reverse=True)
        return Response(pdf_files, status=status.HTTP_200_OK)

    @action(detail=True, methods=['get'])
    def descargar(self, request, pk=None):
        """Descarga un ticket en PDF"""
        if not pk.endswith('.pdf'):
            pk += '.pdf'
        
        file_path = os.path.abspath(os.path.join('Caja', 'media', 'pdf_ticket', pk))
        print(f"Buscando archivo en: {file_path}")
        if not os.path.exists(file_path):
            return Response({'message': 'Archivo no encontrado'}, status=status.HTTP_404_NOT_FOUND)

        return FileResponse(open(file_path, 'rb'), content_type='application/pdf')

    @action(detail=False, methods=['post'])
    def generar_ticket(self, request):
        """Genera un ticket en PDF"""
        productos = Producto.objects.all()
        carrito_productos = CarritoProducto.objects.select_related('producto').all()
        total_productos = sum([cp.cantidad for cp in carrito_productos])
        total_costo_productos = sum([cp.subtotal for cp in carrito_productos])
        
        fecha_actual = datetime.datetime.now().strftime("%d/%m/%Y")
        hora_actual = datetime.datetime.now().strftime("%H:%M:%S")

        context = {
            'productos': productos,
            'carritoProductos': carrito_productos,
            'total_productos': total_productos,
            'total_costo_productos': total_costo_productos,
            'fecha_actual': fecha_actual,
            'hora_actual': hora_actual,
            'usuario': request.user
        }

        html = render_to_string('ticket_pdf.html', context)
        pdf_dir = os.path.join(settings.BASE_DIR, 'Caja', 'media', 'pdf_ticket')
        os.makedirs(pdf_dir, exist_ok=True)
        pdf_path = os.path.join(pdf_dir, f'ticket_{datetime.datetime.now().strftime("%Y%m%d%H%M%S")}.pdf')

        with open(pdf_path, "wb") as pdf_file:
            pisa.CreatePDF(io.BytesIO(html.encode("UTF-8")), dest=pdf_file, encoding='UTF-8')

        return Response({'message': 'Ticket generado', 'ticket_path': pdf_path}, status=status.HTTP_201_CREATED)
