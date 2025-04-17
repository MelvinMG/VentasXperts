from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ProductoViewSet, CarritoProductoViewSet, VentaViewSet, TicketViewSet, CategoriaViewSet, ProveedorViewSet, carritoViewSet, UserViewSet, CajaViewSet

# Creamos un router de DRF para gestionar las rutas de los ViewSets
router = DefaultRouter()
router.register(r'productos', ProductoViewSet, basename='producto')
router.register(r'carritoProducto', CarritoProductoViewSet, basename='carritoProducto')
router.register(r'carrito', carritoViewSet, basename='carrito')
router.register(r'ventas', VentaViewSet, basename='venta')
router.register(r'tickets', TicketViewSet, basename='ticket')
router.register(r'categorias', CategoriaViewSet, basename='categoria')
router.register(r'proveedores', ProveedorViewSet, basename='proveedor')
router.register(r'cajas', CajaViewSet, basename='caja')

urlpatterns = [
    path('', include(router.urls)),  # Incluye las rutas generadas por el router
    path('usuarios/crear/', UserViewSet.as_view({'post': 'create_user'}), name='crear_usuario'),
]
