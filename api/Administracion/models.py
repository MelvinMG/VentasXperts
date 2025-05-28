from django.db import models
from django.contrib.auth.models import User

"""
Creacion de la tabla de Persona  que se relaciona con la tabla de User
Este modulo de pertece a Usuarios y Permisos

La apliaccion de Usuarios y Permisos se va juntar a Administracion debido ah que son los mismos roles y permisos
"""
class Persona(models.Model):
    GENERO_CHOICES = [
        ('M', 'Masculino'),
        ('F', 'Femenino'),
        ('N', 'Prefiero no decirlo'),
        ('D', 'Desconocido'),
    ]

    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name="persona")
    nombre = models.CharField(max_length=100)
    apPaterno = models.CharField(max_length=100)
    apMaterno = models.CharField(max_length=100, blank=True, null=True)
    genero = models.CharField(max_length=1, choices=GENERO_CHOICES, default='D')
    correo = models.EmailField(max_length=200)
    telefono = models.CharField(max_length=15)
    rfc=models.CharField(max_length=100)
    curp=models.CharField(max_length=100)
    foto = models.ImageField(upload_to='perfil_fotos/', blank=True, null=True)
   
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Persona'
        verbose_name_plural = 'Personas'

    def __str__(self):
        return f"{self.nombre} {self.apPaterno} {self.apMaterno}"

#? Tabla de Categorias le pertenece a Administracion
class Categoria(models.Model):
    nombre = models.CharField(max_length=255)
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Categoria'
        verbose_name_plural = 'Categorias'

#? Tabla de Proveedores le pertenece a Administracion
class Proveedor(models.Model):
    nombre = models.CharField(max_length=255)
    telefono = models.CharField(max_length=15)
    correo = models.EmailField()
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Proveedor'
        verbose_name_plural = 'Proveedores'

#? Tabla de Productos le pertenece a Administracion
#** Este modulo va aparecer en la parte de Catalogo
class Producto(models.Model):
    codigo = models.CharField(max_length=100, unique=True)
    nombre = models.CharField(max_length=255)
    categoria = models.ForeignKey('Categoria', on_delete=models.CASCADE)
    proveedor = models.ForeignKey(Proveedor, null=True, blank=True, on_delete=models.SET_NULL)
    stock_Inventario = models.IntegerField()
    stock_Minimo = models.IntegerField()
    precio_proveedor = models.DecimalField(max_digits=10, decimal_places=2)
    precio_tienda = models.DecimalField(max_digits=10, decimal_places=2)
    ganancia_porcentaje = models.DecimalField(max_digits=5, decimal_places=2, blank=True, null=True)
    ganancia_pesos = models.DecimalField(max_digits=10, decimal_places=2, blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def save(self, *args, **kwargs):
        self.ganancia_pesos = self.precio_tienda - self.precio_proveedor
        if self.precio_proveedor > 0:
            self.ganancia_porcentaje = (self.ganancia_pesos / self.precio_proveedor) * 100
        super().save(*args, **kwargs)

    def __str__(self):
        return self.nombre

    class Meta:
        db_table = 'Producto'
        verbose_name_plural = 'Productos'

#? Tabla de Cajas le pertenece a Caja
class Caja(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    saldo_actual = models.DecimalField(max_digits=10, decimal_places=2)
    corte_caja = models.DecimalField(max_digits=10, decimal_places=2)
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Caja'
        verbose_name_plural = 'Cajas'

#? Tabla de Carrito le pertenece a Caja
class Carrito(models.Model):
    precio_total = models.DecimalField(max_digits=10, decimal_places=2)
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Carrito'
        verbose_name_plural = 'Carritos'

#? Tabla de Carrito_x_Producto le pertenece a Caja
class CarritoProducto(models.Model):
    carrito = models.ForeignKey(Carrito, on_delete=models.CASCADE, null=True)
    producto = models.ForeignKey(Producto, on_delete=models.CASCADE)
    cantidad = models.IntegerField(null=True)
    subtotal = models.DecimalField(max_digits=10, decimal_places=2, null=True)

    class Meta:
        db_table = 'Carrito_x_Producto'
        verbose_name_plural = 'Carritos por Productos'

#? Tabla de Carrito_x_Producto le pertenece a Caja
class Venta(models.Model):
    carrito = models.ForeignKey(Carrito, on_delete=models.CASCADE)
    caja = models.ForeignKey('Caja', on_delete=models.CASCADE)
    finanzas = models.ForeignKey('Finanzas', on_delete=models.CASCADE)
    total = models.DecimalField(max_digits=10, decimal_places=2)
    fecha = models.DateTimeField()
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Venta'
        verbose_name_plural = 'Ventas'

#? Tabla de Carrito_x_Producto le pertenece a Administracion
class Finanzas(models.Model):
    fecha = models.DateField()
    hora = models.TimeField()
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)

    class Meta:
        db_table = 'Finanza'
        verbose_name_plural = 'Finanzas'

#? Modelo de Bitacora para auditoría de acciones
class Bitacora(models.Model):
    ACCION_CHOICES = [
        ('create', 'Crear'),
        ('update', 'Actualizar'),
        ('delete', 'Eliminar'),
        ('purchase', 'Compra'),
        ('login', 'Inicio de Sesión'),
        ('logout', 'Cierre de Sesión'),
    ]

    usuario = models.ForeignKey(User, on_delete=models.CASCADE, null=True)
    persona = models.ForeignKey(Persona, on_delete=models.SET_NULL, null=True, blank=True)
    rol = models.CharField(max_length=100, null=True, blank=True)
    accion = models.CharField(max_length=50, choices=ACCION_CHOICES)
    detalle = models.TextField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    update_at = models.DateTimeField(auto_now=True)
    class Meta:
        db_table = 'Bitacora'
        verbose_name_plural = 'Bitacoras'
        indexes = [
            models.Index(fields=['usuario']),
            models.Index(fields=['rol']),
            models.Index(fields=['created_at']),
        ]
    def __str__(self):
        return f"{self.usuario.username} - {self.accion}"
