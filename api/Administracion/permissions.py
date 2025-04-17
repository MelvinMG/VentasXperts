from rest_framework.permissions import BasePermission

class IsRoleUser(BasePermission):
    """
    Permiso personalizado que permite el acceso a usuarios según su rol (Administrador, Gerente, Cajero).
    """
    def has_permission(self, request, view):
        # Los roles permitidos
        allowed_roles = ['Administrador', 'Gerente', 'Cajero']

        # Verifica si el usuario pertenece a uno de los roles permitidos
        return request.user and request.user.groups.filter(name__in=allowed_roles).exists()
