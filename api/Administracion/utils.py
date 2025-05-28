from .models import Bitacora, Persona
from django.contrib.auth.models import User

def registrar_bitacora(usuario: User, accion: str, detalle: str = "", persona: Persona = None):
    rol = None
    if usuario:
        roles = usuario.groups.values_list('name', flat=True)
        rol = ", ".join(roles) if roles else None

    Bitacora.objects.create(
        usuario=usuario,
        persona=persona,
        rol=rol,
        accion=accion,
        detalle=detalle
    )
