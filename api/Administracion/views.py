from rest_framework import viewsets, status
from rest_framework.decorators import action
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from django.contrib.auth.models import User, Group
from django.db import transaction
from rest_framework.exceptions import ValidationError

from .models import Persona
from .serializers import UserSerializer, PersonaSerializer
from .permissions import IsRoleUser
from .adapters import UserAdapter

from django.contrib.auth import authenticate, login, logout
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.utils.decorators import method_decorator
from django.views.decorators.csrf import csrf_exempt

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status



class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def get_permissions(self):
        if self.action in ['retrieve', 'update', 'partial_update', 'destroy']:
            permission_classes = [IsRoleUser]
        else:
            permission_classes = [IsAuthenticated]
        return [permission() for permission in permission_classes]


    # Verificacion de roles para cada usuario
    @action(detail=False, methods=['get'], url_path='me')
    def me(self, request):
        user = request.user
        roles = user.groups.values_list('name', flat=True)

        # Acceder a la relación OneToOne con Persona
        try:
            persona = user.persona  # gracias a related_name="persona"
            nombre = persona.nombre
            apPaterno = persona.apPaterno
        except Persona.DoesNotExist:
            nombre = None
            apPaterno = None

        data = {
            "id": user.id,
            "username": user.username,
            "email": user.email,
            "roles": list(roles),
            "nombre": nombre,
            "apPaterno": apPaterno,
        }
        return Response(data)


    @transaction.atomic
    @action(detail=False, methods=['get'])
    def list_users(self, request):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para ver esta información.'}, status=status.HTTP_403_FORBIDDEN)

        users_data = []
        for user in User.objects.all():
            persona = Persona.objects.filter(user=user).first()
            user_data = UserSerializer(user).data
            
            # Obtener roles (nombres de grupos) y agregarlos a user_data
            roles = user.groups.values_list('name', flat=True)
            user_data['roles'] = list(roles)
            
            if persona:
                user_data.update(PersonaSerializer(persona).data)
            
            users_data.append(user_data)

        return Response(users_data, status=status.HTTP_200_OK)


    @transaction.atomic
    @action(detail=False, methods=['post'])
    def create_user(self, request):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permiso para crear usuarios.'}, status=status.HTTP_403_FORBIDDEN)

        user_data = request.data.get('user')
        persona_data = request.data.get('persona')

        if not user_data or not persona_data:
            return Response({'detail': 'Datos del usuario o persona incompletos.'}, status=status.HTTP_400_BAD_REQUEST)

        user_serializer = UserSerializer(data=user_data)
        persona_serializer = PersonaSerializer(data=persona_data)

        try:
            user, persona = UserAdapter.create_user(user_serializer, persona_serializer, request.user)
            return Response({
                'user': UserSerializer(user).data,
                'persona': PersonaSerializer(persona).data
            }, status=status.HTTP_201_CREATED)
        except ValidationError as e:
            return Response(e.detail, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=True, methods=['put'])
    def update_user(self, request, pk=None):
        user = self.get_object()
        if not request.user.groups.filter(name='Administrador').exists() and request.user.id != user.id:
            return Response({'detail': 'No tienes permiso para actualizar este usuario.'}, status=status.HTTP_403_FORBIDDEN)

        user_data = request.data.get('user')
        persona_data = request.data.get('persona')
        password_actual = request.data.get('password_actual')
        password_nueva = request.data.get('new_password')

        user_serializer = UserSerializer(user, data=user_data, partial=True)
        persona = Persona.objects.get(user=user)
        persona_serializer = PersonaSerializer(persona, data=persona_data, partial=True)

        try:
            UserAdapter(user).update_user(user_serializer, persona_serializer, password_actual, password_nueva, request.user)
            return Response({
                'user': user_serializer.data,
                'persona': persona_serializer.data
            }, status=status.HTTP_200_OK)
        except ValidationError as e:
            return Response(e.detail, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=True, methods=['delete'])
    def delete_user(self, request, pk=None):
        user = self.get_object()
        if not request.user.groups.filter(name='Administrador').exists() and request.user.id != user.id:
            return Response({'detail': 'No tienes permiso para eliminar este usuario.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            UserAdapter(user).delete_user(request.user)
            return Response({'detail': 'Usuario y persona eliminados correctamente.'}, status=status.HTTP_204_NO_CONTENT)
        except Exception as e:
            return Response({'detail': f'Error al eliminar: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=False, methods=['post'], url_path='assign_role_to_user')
    def assign_role_to_user(self, request):
        """
        Asigna un grupo (rol) a un usuario por su nombre de grupo.
        """
        print("DATA RECIBIDA:", request.data)

        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para asignar roles.'}, status=status.HTTP_403_FORBIDDEN)

        username = request.data.get('username', '').strip()
        group_name = request.data.get('group_name', '').strip()

        if not username or not group_name:
            return Response({'detail': 'Faltan datos para asignar el rol.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            user = User.objects.get(username=username)
            group = Group.objects.get(name=group_name)
            user.groups.add(group)

            return Response({'detail': f'Rol "{group_name}" asignado correctamente a {username}.'}, status=status.HTTP_200_OK)

        except User.DoesNotExist:
            return Response({'detail': 'Usuario no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Group.DoesNotExist:
            return Response({'detail': 'Grupo no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al asignar rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=False, methods=['get'])
    def list_roles(self, request):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para ver los roles.'}, status=status.HTTP_403_FORBIDDEN)
        try:
            roles = Group.objects.all()
            roles_data = [{'id': role.id, 'name': role.name} for role in roles]
            return Response(roles_data, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({'detail': f'Error al obtener los roles: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=False, methods=['post'])
    def create_role(self, request):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para crear roles.'}, status=status.HTTP_403_FORBIDDEN)

        role_data = request.data.get('role')
        if not role_data:
            return Response({'detail': 'El nombre del rol es requerido.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            group, created = Group.objects.get_or_create(name=role_data)
            if created:
                return Response({'detail': f'Rol "{role_data}" creado exitosamente.'}, status=status.HTTP_201_CREATED)
            return Response({'detail': f'El rol "{role_data}" ya existe.'}, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({'detail': f'Error al crear el rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=True, methods=['put'])
    def update_role(self, request, pk=None):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para actualizar roles.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            group = Group.objects.get(id=pk)
            new_role_name = request.data.get('role')
            if not new_role_name:
                return Response({'detail': 'El nombre del rol es requerido.'}, status=status.HTTP_400_BAD_REQUEST)
            group.name = new_role_name
            group.save()
            return Response({'detail': f'Rol actualizado a "{new_role_name}"'}, status=status.HTTP_200_OK)
        except Group.DoesNotExist:
            return Response({'detail': 'Rol no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al actualizar el rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=True, methods=['delete'])
    def delete_role(self, request, pk=None):
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para eliminar roles.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            group = Group.objects.get(id=pk)
            group.delete()
            return Response({'detail': 'Rol eliminado correctamente.'}, status=status.HTTP_204_NO_CONTENT)
        except Group.DoesNotExist:
            return Response({'detail': 'Rol no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al eliminar el rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)