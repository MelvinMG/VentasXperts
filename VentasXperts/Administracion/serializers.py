from django.contrib.auth.models import User, Group
from rest_framework import serializers
from .models import Persona 

class PersonaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Persona
        fields = ['id', 'user', 'nombre', 'apPaterno', 'apMaterno', 'genero', 'correo', 'telefono', 'rfc', 'curp', 'created_at', 'updated_at']

class UserSerializer(serializers.ModelSerializer):
    persona = PersonaSerializer() # relacion con la tabla persona

    class Meta:
        model = User
        fields = ['id', 'username', 'email', 'first_name', 'last_name', 'persona']

class GroupSerializer(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = ['id', 'name']
        