from django.contrib.auth.models import Group, User
from django.db.models.signals import post_migrate
from django.dispatch import receiver

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