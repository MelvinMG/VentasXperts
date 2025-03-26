from django.contrib import admin
from .models import Persona, Categoria, Proveedor,Producto,Caja,Carrito,CarritoProducto

admin.site.register(Persona)
admin.site.register(Categoria)
admin.site.register(Proveedor)
admin.site.register(Producto)
admin.site.register(Caja)
admin.site.register(Carrito)
admin.site.register(CarritoProducto)

# Register your models here.
