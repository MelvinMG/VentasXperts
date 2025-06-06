from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.exceptions import ValidationError
from django.db import transaction

from Catalogo.factories.producto_factory import ProductoFactory
from Catalogo.permissions import IsAdministradorOrGerente
from Catalogo.serializers import ProductoSerializer, CategoriaSerializer, ProveedorSerializer, TiendaSerializer, TiendaDetalleSerializer
from Administracion.models import *

from django.template.loader import render_to_string
from xhtml2pdf import pisa
from io import BytesIO
from django.http import FileResponse


class ProveedorViewSet(viewsets.ModelViewSet):
    queryset = Proveedor.objects.all()
    serializer_class = ProveedorSerializer

class CategoriaViewSet(viewsets.ModelViewSet):
    queryset = Categoria.objects.all()
    serializer_class = CategoriaSerializer
    
class TiendaViewSet(viewsets.ModelViewSet):
    queryset = Tienda.objects.all()
    serializer_class = TiendaDetalleSerializer

    @action(detail=True, methods=['get'], url_path='productos')
    def listar_productos(self, request, pk=None):
        try:
            tienda = self.get_object()
            productos = Producto.objects.filter(productotienda__tienda=tienda)
            serializer = ProductoSerializer(productos, many=True)
            return Response(serializer.data)
        except Tienda.DoesNotExist:
            return Response({'error': 'Tienda no encontrada'}, status=404)
    
    @action(detail=True, methods=['post'], url_path='asignar_productos')
    def asignar_productos(self, request, pk=None):
        tienda = self.get_object()
        productos_ids = request.data.get('productos', [])

        if not isinstance(productos_ids, list):
            return Response({'error': 'El campo "productos" debe ser una lista de IDs.'},
                            status=status.HTTP_400_BAD_REQUEST)

        productos = Producto.objects.filter(id__in=productos_ids)

        # Crear relaciones si no existen
        nuevos = 0
        for producto in productos:
            _, created = ProductoTienda.objects.get_or_create(tienda=tienda, producto=producto)
            if created:
                nuevos += 1

        return Response({
            'message': f'Se asignaron {nuevos} productos a la tienda "{tienda.nombre}".'
        }, status=status.HTTP_200_OK)
    
class ProductoViewSet(viewsets.ModelViewSet):
    queryset = Producto.objects.all()
    serializer_class = ProductoSerializer

    @action(detail=False, methods=['post'], url_path='crear_producto', permission_classes=[IsAdministradorOrGerente])
    @transaction.atomic
    def crear_producto(self, request):
        try:
            data = request.data
            producto_data = {
                "codigo": data.get("codigo"),
                "nombre": data.get("nombre"),
                "categoria_id": data.get("categoria_id"),
                "proveedor_id": data.get("proveedor_id"),  # opcional
                "stock_Inventario": data.get("stock_Inventario"),
                "stock_Minimo": data.get("stock_Minimo"),
                "precio_proveedor": data.get("precio_proveedor"),
                "precio_tienda": data.get("precio_tienda"),
                "ganancia_porcentaje": data.get("ganancia_porcentaje"),
                "ganancia_pesos": data.get("ganancia_pesos"),
            }

            producto = ProductoFactory.crear_producto_con_dependencias(producto_data)

            user = request.user
            persona = Persona.objects.filter(user=user).first()
            rol = user.groups.first().name if user.groups.exists() else 'Sin rol'

            Bitacora.objects.create(
                usuario=user,
                persona=persona,
                rol=rol,
                accion='create',
                detalle=f'Se creó el producto "{producto.nombre}" con código {producto.codigo}.'
            )

            return Response({
                'message': 'Producto creado exitosamente',
                'producto': ProductoSerializer(producto).data
            }, status=status.HTTP_201_CREATED)

        except ValidationError as e:
            return Response({'error': e.detail}, status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        
    @action(detail=True, methods=['put'], url_path='modificar', permission_classes=[IsAdministradorOrGerente])
    @transaction.atomic
    def modificar_producto(self, request, pk=None):
        try:
            producto = self.get_object()

            for field in [
                'nombre', 'stock_Inventario', 'stock_Minimo',
                'precio_proveedor', 'precio_tienda'
            ]:
                if field in request.data:
                    setattr(producto, field, request.data[field])

            producto.save()

            user = request.user
            persona = Persona.objects.filter(user=user).first()
            rol = user.groups.first().name if user.groups.exists() else 'Sin rol'

            Bitacora.objects.create(
                usuario=user,
                persona=persona,
                rol=rol,
                accion='update',
                detalle=f'Se actualizó el producto "{producto.nombre}" con ID {producto.id}.'
            )

            return Response({
                'message': 'Producto modificado correctamente',
                'producto': ProductoSerializer(producto).data
            }, status=status.HTTP_200_OK)

        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        
        
    @action(detail=True, methods=['delete'], url_path='eliminar', permission_classes=[IsAdministradorOrGerente])
    @transaction.atomic
    def eliminar_producto(self, request, pk=None):
        try:
            producto = self.get_object()
            nombre = producto.nombre
            producto.delete()

            user = request.user
            persona = Persona.objects.filter(user=user).first()
            rol = user.groups.first().name if user.groups.exists() else 'Sin rol'

            Bitacora.objects.create(
                usuario=user,
                persona=persona,
                rol=rol,
                accion='delete',
                detalle=f'Se eliminó el producto "{nombre}".'
            )

            return Response({'message': f'Producto "{nombre}" eliminado correctamente'}, status=status.HTTP_204_NO_CONTENT)

        except Exception as e:
            return Response({'error': str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
    
    @action(detail=False, methods=['get'], url_path='buscar_filtrar', permission_classes=[IsAdministradorOrGerente])
    def buscar_filtrar(self, request):
        nombre = request.query_params.get('nombre', '')
        estado = request.query_params.get('estado', '').lower()

        productos = Producto.objects.all()

        if nombre:
            productos = productos.filter(nombre__icontains=nombre)

        if estado:
            if estado == 'suficiente':
                productos = productos.filter(stock_Inventario__gt=models.F('stock_Minimo'))
            elif estado == 'carente':
                productos = productos.filter(stock_Inventario__lt=models.F('stock_Minimo'), stock_Inventario__gt=0)
            elif estado == 'agotado':
                productos = productos.filter(stock_Inventario=0)
        
        if not productos.exists():
            return Response({'message': 'Producto no encontrado'}, status=status.HTTP_404_NOT_FOUND)


        serializer = ProductoSerializer(productos, many=True)
        return Response(serializer.data, status=status.HTTP_200_OK)
    
    
    
    @action(detail=False, methods=['get'], url_path='reporte_stock', permission_classes=[IsAdministradorOrGerente])
    def reporte_stock(self, request):
        suficientes_qs = Producto.objects.filter(stock_Inventario__gt=models.F('stock_Minimo'))
        carentes_qs = Producto.objects.filter(stock_Inventario__lt=models.F('stock_Minimo'), stock_Inventario__gt=0)
        agotados_qs = Producto.objects.filter(stock_Inventario=0)

        serializer_suf = ProductoSerializer(suficientes_qs, many=True)
        serializer_car = ProductoSerializer(carentes_qs, many=True)
        serializer_agot = ProductoSerializer(agotados_qs, many=True)

        return Response({
            "total_productos": Producto.objects.count(),
            "suficientes": {
                "cantidad": suficientes_qs.count(),
                "productos": serializer_suf.data
            },
            "carentes": {
                "cantidad": carentes_qs.count(),
                "productos": serializer_car.data
            },
            "agotados": {
                "cantidad": agotados_qs.count(),
                "productos": serializer_agot.data
            }
        }, status=status.HTTP_200_OK)



    @action(detail=False, methods=['get'], url_path='reporte_stock_pdf', permission_classes=[IsAdministradorOrGerente])
    def generar_reporte_pdf(self, request):
        try:
            suficientes = Producto.objects.filter(stock_Inventario__gt=models.F('stock_Minimo'))
            carentes = Producto.objects.filter(stock_Inventario__lt=models.F('stock_Minimo'), stock_Inventario__gt=0)
            agotados = Producto.objects.filter(stock_Inventario=0)

            context = {
                'total': Producto.objects.count(),
                'suficientes': suficientes,
                'carentes': carentes,
                'agotados': agotados
            }

            html = render_to_string('reportes/reporte_stock.html', context)

            resultado = BytesIO()
            print(html)
            pdf = pisa.pisaDocument(BytesIO(html.encode("UTF-8")), resultado)

            if not pdf.err:
                return FileResponse(resultado, as_attachment=True, filename="reporte_stock.pdf")
            else:
                return Response({'error': 'Error al generar el PDF'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

        except Exception as e:
            return Response({'error': f'Excepción: {str(e)}'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)