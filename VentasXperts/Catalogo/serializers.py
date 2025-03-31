from Administracion.models import *  # Asegúrate de tener importado el modelo Producto
from rest_framework import serializers

class ProductoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Producto
        fields = '__all__'  # O puedes listar solo los campos que quieres exponer
        read_only_fields = ['id', 'created_at', 'updated_at', 'ganancia_pesos', 'ganancia_porcentaje']



class CategoriaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Categoria
        fields = '__all__'
        read_only_fields = ['id', 'created_at', 'update_at']




class ProveedorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Proveedor
        fields = '__all__'
        read_only_fields = ['id', 'created_at', 'update_at']