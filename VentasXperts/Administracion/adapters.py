# adapters.py
from django.contrib.auth.models import User, Group
from django.contrib.auth.hashers import make_password
from .models import Persona, Bitacora
from rest_framework.exceptions import ValidationError
from django.contrib.auth.hashers import make_password

class UserAdapter:
    def __init__(self, user=None):
        self.user = user

    from django.contrib.auth.hashers import make_password

    @staticmethod
    def create_user(user_serializer, persona_serializer, created_by):
        user_is_valid = user_serializer.is_valid()
        persona_is_valid = persona_serializer.is_valid()

        if user_is_valid and persona_is_valid:
            # Extraer los datos validados
            user_data = user_serializer.validated_data

            # Cifrar la contraseña antes de crear el usuario
            user_data['password'] = make_password(user_data['password'])

            # Crear el usuario manualmente con los datos
            user = User.objects.create(**user_data)

            # Crear la persona asociada
            persona = persona_serializer.save(user=user)

            # Registrar en bitácora
            Bitacora.objects.create(
                usuario=created_by,
                persona=getattr(created_by, 'persona', None),
                rol=getattr(created_by, 'usuariorol').rol if hasattr(created_by, 'usuariorol') else None,
                accion='create',
                detalle=f"Usuario {user.username} añadido."
            )

            return user, persona

        else:
            raise ValidationError({
                'user_errors': user_serializer.errors,
                'persona_errors': persona_serializer.errors
            })


    def update_user(self, user_serializer, persona_serializer, password_actual, password_nueva, updated_by):
        if password_nueva and password_actual:
            if self.user.check_password(password_actual):
                self.user.password = make_password(password_nueva)
                self.user.save()
            else:
                raise ValidationError({'password_actual': ['Contraseña actual incorrecta.']})

        if user_serializer.is_valid():
            user_serializer.save()

        if persona_serializer.is_valid():
            persona_serializer.save()

        Bitacora.objects.create(
            usuario=updated_by,
            persona=getattr(updated_by, 'persona', None),
            rol=getattr(updated_by, 'usuariorol').rol if hasattr(updated_by, 'usuariorol') else None,
            accion='update',
            detalle=f"Usuario {self.user.username} actualizado."
        )

    def delete_user(self, deleted_by):
        if hasattr(self.user, 'persona'):
            self.user.persona.delete()

        Bitacora.objects.create(
            usuario=deleted_by,
            persona=getattr(deleted_by, 'persona', None),
            rol=getattr(deleted_by, 'usuariorol').rol if hasattr(deleted_by, 'usuariorol') else None,
            accion='delete',
            detalle=f"Usuario {self.user.username} eliminado."
        )

        self.user.delete()

    def assign_role(self, group_name, assigned_by):
        try:
            group = Group.objects.get(name=group_name)
            self.user.groups.add(group)

            Bitacora.objects.create(
                usuario=assigned_by,
                persona=getattr(assigned_by, 'persona', None),
                rol=None,
                accion='update',
                detalle=f"Rol {group.name} asignado a {self.user.username}."
            )

            return {'success': True}
        except Group.DoesNotExist:
            return {'success': False, 'error': 'Grupo no encontrado'}

