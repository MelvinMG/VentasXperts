# permissions.py
from rest_framework.permissions import BasePermission

class IsAdministradorOrGerente(BasePermission):
    def has_permission(self, request, view):
        return request.user.groups.filter(name__in=['Administrador', 'Gerente']).exists()
