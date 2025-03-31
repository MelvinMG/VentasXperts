from Administracion.models import Producto, Categoria, Proveedor
from rest_framework.exceptions import ValidationError

class ProductoFactory:
    @staticmethod
    def crear_producto_con_dependencias(data):
        categoria_nombre = data.pop('categoria_nombre', None)
        proveedor_nombre = data.pop('proveedor_nombre', None)

        if not categoria_nombre:
            raise ValidationError({'categoria_nombre': 'Este campo es requerido.'})

        if not proveedor_nombre:
            raise ValidationError({'proveedor_nombre': 'Este campo es requerido.'})

        categoria, _ = Categoria.objects.get_or_create(nombre=categoria_nombre)
        proveedor, _ = Proveedor.objects.get_or_create(nombre=proveedor_nombre, defaults={
            'telefono': data.get('proveedor_telefono', '0000000000'),
            'correo': data.get('proveedor_correo', 'proveedor@demo.com')
        })

        producto = Producto.objects.create(
            codigo=data['codigo'],
            nombre=data['nombre'],
            categoria=categoria,
            proveedor=proveedor,
            stock_Inventario=data['stock_Inventario'],
            stock_Minimo=data['stock_Minimo'],
            precio_proveedor=data['precio_proveedor'],
            precio_tienda=data['precio_tienda']
        )

        return producto
