from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework.exceptions import ValidationError
from django.db import transaction

from Catalogo.factories.producto_factory import ProductoFactory
from Catalogo.permissions import IsAdministradorOrGerente
from Catalogo.serializers import ProductoSerializer, CategoriaSerializer, ProveedorSerializer
from Administracion.models import *


class ProveedorViewSet(viewsets.ModelViewSet):
    queryset = Proveedor.objects.all()
    serializer_class = ProveedorSerializer
    permission_classes = [IsAdministradorOrGerente]

class CategoriaViewSet(viewsets.ModelViewSet):
    queryset = Categoria.objects.all()
    serializer_class = CategoriaSerializer
    permission_classes = [IsAdministradorOrGerente]
    
class ProductoViewSet(viewsets.ModelViewSet):
    queryset = Producto.objects.all()
    serializer_class = ProductoSerializer
    permission_classes = [IsAdministradorOrGerente]  # ✅ Solo Administrador o Gerente pueden usar este ViewSet

    @action(detail=False, methods=['post'], url_path='crear_producto', permission_classes=[IsAdministradorOrGerente])
    @transaction.atomic
    def crear_producto(self, request):
        try:
            producto = ProductoFactory.crear_producto_con_dependencias(request.data)

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

            
            
            

