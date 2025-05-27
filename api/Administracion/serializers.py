from django.contrib.auth.models import User, Group
from rest_framework import serializers
from .models import Persona

class PersonaSerializer(serializers.ModelSerializer):
    foto_url = serializers.SerializerMethodField()  # Añadido para exponer la URL de la foto

    class Meta:
        model = Persona
        fields = [
            'id', 'nombre', 'apPaterno', 'apMaterno', 'genero', 'correo',
            'telefono', 'rfc', 'curp', 'created_at', 'updated_at', 'foto_url'
        ]
        read_only_fields = ['id', 'created_at', 'updated_at']

    def get_foto_url(self, obj):
        request = self.context.get('request')
        if obj.foto and hasattr(obj.foto, 'url'):
            return request.build_absolute_uri(obj.foto.url)
        return None

class UserSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True, required=True)
    persona = PersonaSerializer(read_only=True)
    roles = serializers.SerializerMethodField()  # Asumiendo que obtienes roles así

    class Meta:
        model = User
        fields = ['id', 'username', 'email', 'first_name', 'last_name', 'password', 'persona', 'roles']
        read_only_fields = ['id']

    def get_roles(self, obj):
        return list(obj.groups.values_list('name', flat=True))  # ejemplo para obtener grupos como roles

    def create(self, validated_data):
        password = validated_data.pop('password')
        user = User(**validated_data)
        user.set_password(password)
        user.save()
        return user

class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = ['id', 'name']

from Administracion.models import Categoria

class CategoriaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Categoria
        fields = '__all__'
        read_only_fields = ['id', 'created_at', 'update_at']
