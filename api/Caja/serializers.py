from django.contrib.auth.models import User
from rest_framework import serializers
from Administracion.models import Persona, Categoria, Proveedor, Producto, Caja, Carrito, CarritoProducto, Venta, Finanzas

# Serializer para el usuario
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'email']
        
class UserCreateSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)

    class Meta:
        model = User
        fields = ['username', 'email', 'password']

    def create(self, validated_data):
        user = User.objects.create_user(
            username=validated_data['username'],
            email=validated_data['email'],
            password=validated_data['password']
        )
        return user

# Serializer para Persona
class PersonaSerializer(serializers.Serializer):
    user = serializers.PrimaryKeyRelatedField(queryset=User.objects.all())
    nombre = serializers.CharField(required=True)
    apPaterno = serializers.CharField(required=True)
    apMaterno = serializers.CharField(required=False, allow_null=True, allow_blank=True)
    genero = serializers.ChoiceField(choices=Persona.GENERO_CHOICES, default='D')
    correo = serializers.EmailField(required=True)
    telefono = serializers.CharField(required=True)
    rfc = serializers.CharField(required=True)
    curp = serializers.CharField(required=True)
    created_at = serializers.DateTimeField(read_only=True)
    updated_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Persona.objects.create(**validated_data)

    def update(self, instance, validated_data):
        for attr, value in validated_data.items():
            setattr(instance, attr, value)
        instance.save()
        return instance

# Serializer para Categoría
class CategoriaSerializer(serializers.Serializer):
    nombre = serializers.CharField(required=True)
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Categoria.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.nombre = validated_data.get('nombre', instance.nombre)
        instance.save()
        return instance

# Serializer para Proveedor
class ProveedorSerializer(serializers.Serializer):
    nombre = serializers.CharField(required=True)
    telefono = serializers.CharField(required=True)
    correo = serializers.EmailField(required=True)
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Proveedor.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.nombre = validated_data.get('nombre', instance.nombre)
        instance.telefono = validated_data.get('telefono', instance.telefono)
        instance.correo = validated_data.get('correo', instance.correo)
        instance.save()
        return instance

# Serializer para Producto
class ProductoSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    codigo = serializers.CharField(required=True)
    nombre = serializers.CharField(required=True)
    categoria = serializers.PrimaryKeyRelatedField(queryset=Categoria.objects.all())
    proveedor = serializers.PrimaryKeyRelatedField(queryset=Proveedor.objects.all(), allow_null=True)
    stock_Inventario = serializers.IntegerField(required=True)
    stock_Minimo = serializers.IntegerField(required=True)
    precio_proveedor = serializers.DecimalField(max_digits=10, decimal_places=2)
    precio_tienda = serializers.DecimalField(max_digits=10, decimal_places=2)
    ganancia_porcentaje = serializers.DecimalField(max_digits=5, decimal_places=2, required=False, allow_null=True)
    ganancia_pesos = serializers.DecimalField(max_digits=10, decimal_places=2, required=False, allow_null=True)
    created_at = serializers.DateTimeField(read_only=True)
    updated_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Producto.objects.create(**validated_data)

    def update(self, instance, validated_data):
        for attr, value in validated_data.items():
            setattr(instance, attr, value)
        instance.save()
        return instance

# Serializer para Caja
class CajaSerializer(serializers.Serializer):
    user = serializers.PrimaryKeyRelatedField(queryset=User.objects.all())
    saldo_actual = serializers.DecimalField(max_digits=10, decimal_places=2)
    corte_caja = serializers.DecimalField(max_digits=10, decimal_places=2)
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Caja.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.saldo_actual = validated_data.get('saldo_actual', instance.saldo_actual)
        instance.corte_caja = validated_data.get('corte_caja', instance.corte_caja)
        instance.save()
        return instance

# Serializer para Carrito
class CarritoSerializer(serializers.Serializer):
    precio_total = serializers.DecimalField(max_digits=10, decimal_places=2)
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Carrito.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.precio_total = validated_data.get('precio_total', instance.precio_total)
        instance.save()
        return instance

# Serializer para CarritoProducto
class CarritoProductoSerializer(serializers.Serializer):
    carrito = serializers.PrimaryKeyRelatedField(queryset=Carrito.objects.all())
    producto = ProductoSerializer(read_only=True)  # Detalles del producto anidados
    producto_id = serializers.PrimaryKeyRelatedField(
        queryset=Producto.objects.all(), source='producto', write_only=True
    )
    cantidad = serializers.IntegerField()
    subtotal = serializers.DecimalField(max_digits=10, decimal_places=2)

    def create(self, validated_data):
        return CarritoProducto.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.cantidad = validated_data.get('cantidad', instance.cantidad)
        instance.subtotal = validated_data.get('subtotal', instance.subtotal)
        instance.save()
        return instance

# Serializer para Venta
class VentaSerializer(serializers.Serializer):
    carrito = serializers.PrimaryKeyRelatedField(queryset=Carrito.objects.all())
    caja = serializers.PrimaryKeyRelatedField(queryset=Caja.objects.all())
    finanzas = serializers.PrimaryKeyRelatedField(queryset=Finanzas.objects.all())
    total = serializers.DecimalField(max_digits=10, decimal_places=2)
    fecha = serializers.DateTimeField()
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Venta.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.total = validated_data.get('total', instance.total)
        instance.fecha = validated_data.get('fecha', instance.fecha)
        instance.save()
        return instance

# Serializer para Finanzas
class FinanzasSerializer(serializers.Serializer):
    fecha = serializers.DateField()
    hora = serializers.TimeField()
    created_at = serializers.DateTimeField(read_only=True)
    update_at = serializers.DateTimeField(read_only=True)

    def create(self, validated_data):
        return Finanzas.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.fecha = validated_data.get('fecha', instance.fecha)
        instance.hora = validated_data.get('hora', instance.hora)
        instance.save()
        return instance
