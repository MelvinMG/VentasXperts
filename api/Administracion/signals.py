from django.contrib.auth.models import Group, User
from django.db.models.signals import post_migrate
from django.dispatch import receiver
from django.contrib.auth.signals import user_logged_in, user_logged_out
from .models import Bitacora

@receiver(post_migrate)
def create_default_groups(sender, **kwargs):
    """
    Crear los grupos por defecto (Administrador, Gerente, Cajero) 
    y asignar todos los roles a los superusuarios.
    """
     
    # Crear los grupos si no existen
    administrador, created = Group.objects.get_or_create(name='Administrador')
    gerente, created = Group.objects.get_or_create(name='Gerente')
    cajero, created = Group.objects.get_or_create(name='Cajero')
    # Asignar todos los grupos a los superusuarios
    superusers = User.objects.filter(is_superuser=True)

    for superuser in superusers:
        superuser.groups.add(administrador, gerente, cajero)


@receiver(user_logged_in)
def log_login(sender, request, user, **kwargs):
    Bitacora.objects.create(
        usuario=user,
        persona=getattr(user, 'persona', None),
        rol=', '.join(user.groups.values_list('name', flat=True)),
        accion='login',
        detalle=f'Inicio sesión como "{user.username}"'
    )


@receiver(user_logged_out)
def log_logout(sender, request, user, **kwargs):
    if user is not None:
        Bitacora.objects.create(
            usuario=user,
            persona=getattr(user, 'persona', None),
            rol=', '.join(user.groups.values_list('name', flat=True)),
            accion='logout',
            detalle=f'Cierre sesión como "{user.username}"'
        )