from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import UserViewSet

# Crear un router para registrar el ViewSet
router = DefaultRouter()
router.register(r'users', UserViewSet,basename='users')  # Registro del UserViewSet

urlpatterns = [
    path('', include(router.urls)),  # Registrar las URLs del router
]
