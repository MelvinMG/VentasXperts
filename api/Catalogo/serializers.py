from Administracion.models import *  # Asegúrate de tener importado el modelo Producto
from rest_framework import serializers

class ProductoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Producto
        fields = '__all__'  # O puedes listar solo los campos que quieres exponer
        read_only_fields = ['id', 'created_at', 'updated_at', 'ganancia_pesos', 'ganancia_porcentaje']
    
    def get_estado_stock(self, obj):
        if obj.stock_Inventario == 0:
            return "Agotado"
        elif obj.stock_Inventario < obj.stock_Minimo:
            return "Carente"
        else:
            return "Suficiente"
        
class TiendaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Tienda
        fields = '__all__'
        read_only_fields = ['id', 'created_at', 'update_at']
        
class ProductoTiendaSerializer(serializers.ModelSerializer):
    class Meta:
        model = ProductoTienda
        fields = ['id', 'producto', 'tienda']

class TiendaDetalleSerializer(serializers.ModelSerializer):
    productos = serializers.SerializerMethodField()

    class Meta:
        model = Tienda
        fields = ['id', 'nombre', 'descripcion', 'created_at', 'update_at', 'productos']

    def get_productos(self, obj):
        productos = Producto.objects.filter(productotienda__tienda=obj)
        return ProductoSerializer(productos, many=True).data

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