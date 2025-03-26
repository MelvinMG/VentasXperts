from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),  # Rutas del admin de Django
    path('api/', include('Administracion.urls')),  # Incluir las URLs de la app Administracion
]
