from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),  # Rutas del admin de Django
    path('api/', include('Administracion.urls')),  # Incluir las URLs de la app Administracion
    path('api/caja/', include('Caja.urls')),  # Incluimos las rutas de la app Caja
    path('api/catalogo/', include('Catalogo.urls')),  # Incluimos las rutas de la app Caja
]