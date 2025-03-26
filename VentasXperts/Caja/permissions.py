from rest_framework.permissions import BasePermission
from django.contrib.auth.models import Group

class IsCajero(BasePermission):
    """
    Permite acceso solo a usuarios que pertenezcan al grupo con ID 3 (Cajero).
    """

    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.groups.filter(id=3).exists()
