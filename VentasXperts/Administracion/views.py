from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework import status
from django.contrib.auth.models import User, Group
from .models import Persona
from .serializers import UserSerializer, PersonaSerializer
from rest_framework.permissions import IsAuthenticated
from .permissions import IsRoleUser  # Usamos el permiso para roles
from rest_framework.decorators import action
from django.db import transaction

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

    def get_permissions(self):
        # Aplicar permisos basados en el rol de usuario
        if self.action in ['retrieve', 'update', 'partial_update', 'destroy']:
            permission_classes = [IsRoleUser]
        else:
            permission_classes = [IsAuthenticated]  # Cualquier usuario autenticado puede ver y editar sus datos
        return [permission() for permission in permission_classes]

    @transaction.atomic
    @action(detail=False, methods=['get'])
    def list_users(self, request):
        """
        Listar todos los usuarios con sus datos. Solo accesible para administradores.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para ver esta información.'}, status=status.HTTP_403_FORBIDDEN)

        users = User.objects.all()
        users_data = []

        for user in users:
            persona = Persona.objects.filter(user=user).first()
            if persona:
                user_data = UserSerializer(user).data
                persona_data = PersonaSerializer(persona).data
                user_data.update(persona_data)
                users_data.append(user_data)
            else:
                user_data = UserSerializer(user).data
                users_data.append(user_data)

        return Response(users_data, status=status.HTTP_200_OK)

    @transaction.atomic
    @action(detail=False, methods=['post'])
    def create_user(self, request):
        """
        Crear un usuario junto con su persona asociada. Solo administradores pueden hacer esto.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permiso para crear usuarios.'}, status=status.HTTP_403_FORBIDDEN)

        user_data = request.data.get('user')
        persona_data = request.data.get('persona')

        if not user_data or not persona_data:
            return Response({'detail': 'Datos del usuario o persona incompletos.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            user = User.objects.create_user(
                username=user_data['username'],
                email=user_data['email'],
                password=user_data['password'],
                first_name=user_data['first_name'],
                last_name=user_data['last_name']
            )
            user.save()

            persona = Persona.objects.create(
                user=user,
                nombre=persona_data['nombre'],
                apPaterno=persona_data['apPaterno'],
                apMaterno=persona_data.get('apMaterno', ''),
                genero=persona_data['genero'],
                correo=persona_data['correo'],
                telefono=persona_data['telefono'],
                rfc=persona_data['rfc'],
                curp=persona_data['curp']
            )

            user_serializer = UserSerializer(user)
            persona_serializer = PersonaSerializer(persona)

            return Response({
                'user': user_serializer.data,
                'persona': persona_serializer.data
            }, status=status.HTTP_201_CREATED)

        except KeyError as e:
            return Response({'detail': f'Campo faltante: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=True, methods=['put'])
    def update_user(self, request, pk=None):
        """
        Actualizar los datos del usuario y su persona asociada. Solo administradores pueden hacer esto.
        """
        user = self.get_object()

        if not request.user.groups.filter(name='Administrador').exists() and request.user.id != user.id:
            return Response({'detail': 'No tienes permiso para actualizar este usuario.'}, status=status.HTTP_403_FORBIDDEN)

        user_data = request.data.get('user')
        if user_data:
            user.username = user_data.get('username', user.username)
            user.email = user_data.get('email', user.email)
            user.first_name = user_data.get('first_name', user.first_name)
            user.last_name = user_data.get('last_name', user.last_name)
            password = user_data.get('password')
            if password:
                user.set_password(password)
            user.save()

        persona_data = request.data.get('persona')
        if persona_data:
            persona = Persona.objects.get(user=user)
            persona.nombre = persona_data.get('nombre', persona.nombre)
            persona.apPaterno = persona_data.get('apPaterno', persona.apPaterno)
            persona.apMaterno = persona_data.get('apMaterno', persona.apMaterno)
            persona.genero = persona_data.get('genero', persona.genero)
            persona.correo = persona_data.get('correo', persona.correo)
            persona.telefono = persona_data.get('telefono', persona.telefono)
            persona.rfc = persona_data.get('rfc', persona.rfc)
            persona.curp = persona_data.get('curp', persona.curp)
            persona.save()

        user_serializer = UserSerializer(user)
        persona_serializer = PersonaSerializer(persona)

        return Response({
            'user': user_serializer.data,
            'persona': persona_serializer.data
        }, status=status.HTTP_200_OK)

    @transaction.atomic
    @action(detail=True, methods=['delete'])
    def delete_user(self, request, pk=None):
        """
        Eliminar un usuario y su persona asociada. Solo administradores pueden hacer esto.
        """
        user = self.get_object()

        if not request.user.groups.filter(name='Administrador').exists() and request.user.id != user.id:
            return Response({'detail': 'No tienes permiso para eliminar este usuario.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            persona = Persona.objects.get(user=user)
            persona.delete()
            user.delete()
            return Response({'detail': 'Usuario y persona eliminados correctamente.'}, status=status.HTTP_204_NO_CONTENT)
        except Persona.DoesNotExist:
            return Response({'detail': 'Persona no encontrada para este usuario.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al eliminar: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)


    @transaction.atomic
    @action(detail=False, methods=['get'])
    def list_roles(self, request):
        """
        Listar todos los roles (grupos). Solo accesible para administradores.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para ver los roles.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            # Obtener todos los grupos (roles)
            roles = Group.objects.all()
            roles_data = []

            # Serializar los datos de los roles
            for role in roles:
                roles_data.append({'id': role.id, 'name': role.name})

            return Response(roles_data, status=status.HTTP_200_OK)

        except Exception as e:
            return Response({'detail': f'Error al obtener los roles: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=False, methods=['post'])
    def create_role(self, request):
        """
        Crear un nuevo rol (grupo). Solo accesible para administradores.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para crear roles.'}, status=status.HTTP_403_FORBIDDEN)

        role_data = request.data.get('role')

        if not role_data:
            return Response({'detail': 'El nombre del rol es requerido.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            # Crear el rol (grupo)
            group, created = Group.objects.get_or_create(name=role_data)

            if created:
                return Response({'detail': f'Rol "{role_data}" creado exitosamente.'}, status=status.HTTP_201_CREATED)
            else:
                return Response({'detail': f'El rol "{role_data}" ya existe.'}, status=status.HTTP_200_OK)

        except Exception as e:
            return Response({'detail': f'Error al crear el rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)
        
    @transaction.atomic
    @action(detail=True, methods=['put'])
    def update_role(self, request, pk=None):
        """
        Actualizar el nombre de un rol (grupo). Solo accesible para administradores.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para actualizar roles.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            # Obtener el rol (grupo) por su id (pk)
            group = Group.objects.get(id=pk)

            # Obtener el nuevo nombre del rol
            new_role_name = request.data.get('role')

            if not new_role_name:
                return Response({'detail': 'El nombre del rol es requerido.'}, status=status.HTTP_400_BAD_REQUEST)

            # Actualizar el nombre del rol
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
        """
        Eliminar un rol (grupo). Solo accesible para administradores.
        """
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para eliminar roles.'}, status=status.HTTP_403_FORBIDDEN)

        try:
            # Obtener el rol (grupo) por su id (pk)
            group = Group.objects.get(id=pk)

            # Eliminar el grupo
            group.delete()

            return Response({'detail': 'Rol eliminado correctamente.'}, status=status.HTTP_204_NO_CONTENT)

        except Group.DoesNotExist:
            return Response({'detail': 'Rol no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al eliminar el rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)

    @transaction.atomic
    @action(detail=False, methods=['post'])
    def assign_role_to_user(self, request):
        """
        Asignar un rol (grupo) a un usuario. Solo accesible para administradores.
        """
        # Verificar si el usuario tiene el rol de Administrador
        if not request.user.groups.filter(name='Administrador').exists():
            return Response({'detail': 'No tienes permisos para asignar roles.'}, status=status.HTTP_403_FORBIDDEN)

        # Obtener los datos necesarios de la solicitud
        role_name = request.data.get('role')  # Nombre del rol (grupo) que se asignará
        username = request.data.get('username')  # Nombre de usuario al que se asignará el rol

        # Verificar si los datos están completos
        if not role_name or not username:
            return Response({'detail': 'Faltan datos para asignar el rol.'}, status=status.HTTP_400_BAD_REQUEST)

        try:
            # Obtener o crear el grupo (rol)
            group = Group.objects.get(name=role_name)
            
            # Obtener el usuario por su nombre de usuario
            user = User.objects.get(username=username)

            # Asignar el rol al usuario
            user.groups.add(group)

            return Response({'detail': f'Rol "{role_name}" asignado a {username} correctamente.'}, status=status.HTTP_200_OK)

        except Group.DoesNotExist:
            return Response({'detail': 'Rol no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except User.DoesNotExist:
            return Response({'detail': 'Usuario no encontrado.'}, status=status.HTTP_404_NOT_FOUND)
        except Exception as e:
            return Response({'detail': f'Error al asignar rol: {str(e)}'}, status=status.HTTP_400_BAD_REQUEST)