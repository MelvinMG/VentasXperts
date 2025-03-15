
from django.contrib import admin
from django.urls import path, include

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/caja/', include('Caja.urls')),  # Incluimos las rutas de la app Caja
]
