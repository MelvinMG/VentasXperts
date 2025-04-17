# Catalogo/urls.py
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import ProductoViewSet, CategoriaViewSet, ProveedorViewSet  # Agrega otros si deseas

router = DefaultRouter()
router.register(r'productos', ProductoViewSet, basename='productos')
router.register(r'categorias', CategoriaViewSet, basename='categorias')
router.register(r'proveedores', ProveedorViewSet, basename='proveedores')

urlpatterns = [
    path('', include(router.urls)),  # Esto generará rutas como:
    # POST /api/catalogo/productos/
    # POST /api/catalogo/productos/crear_producto/
]
