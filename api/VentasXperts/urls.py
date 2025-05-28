from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static
from rest_framework_simplejwt.views import (
    TokenObtainPairView,
    TokenRefreshView,
) 

urlpatterns = [
    path('admin/', admin.site.urls),  # Rutas del admin de Django
    path('api/', include('Administracion.urls')),  # Incluir las URLs de la app Administracion
    path('api/caja/', include('Caja.urls')),  # Incluimos las rutas de la app Caja
    path('api/catalogo/', include('Catalogo.urls')),  # Incluimos las rutas de la app Caja
    path('api/token/', TokenObtainPairView.as_view(), name='token_obtain_pair'), # Ruta para obtener el token JWT
    path('api/token/refresh/', TokenRefreshView.as_view(), name='token_refresh'), # Ruta para refrescar el token JWT
]
if settings.DEBUG:
    urlpatterns += static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)