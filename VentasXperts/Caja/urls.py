from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ProductoViewSet, CarritoProductoViewSet, VentaViewSet, TicketViewSet, CategoriaViewSet, ProveedorViewSet

# Creamos un router de DRF para gestionar las rutas de los ViewSets
router = DefaultRouter()
router.register(r'productos', ProductoViewSet, basename='producto')
router.register(r'carrito', CarritoProductoViewSet, basename='carrito')
router.register(r'ventas', VentaViewSet, basename='venta')
router.register(r'tickets', TicketViewSet, basename='ticket')
router.register(r'categorias', CategoriaViewSet, basename='categoria')
router.register(r'proveedores', ProveedorViewSet, basename='proveedor')

urlpatterns = [
    path('', include(router.urls)),  # Incluye las rutas generadas por el router
]
