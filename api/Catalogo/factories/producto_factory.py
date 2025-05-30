from rest_framework.exceptions import ValidationError 
from Administracion.models import Categoria, Proveedor, Producto

class ProductoFactory:
    @staticmethod
    def crear_producto_con_dependencias(data):
        categoria_id = data.get('categoria_id')
        proveedor_id = data.get('proveedor_id', None)  # Opcional

        if not categoria_id:
            raise ValidationError({'categoria_id': 'Este campo es requerido.'})

        try:
            categoria = Categoria.objects.get(id=categoria_id)
        except Categoria.DoesNotExist:
            raise ValidationError({'categoria_id': 'Categoría no encontrada.'})

        proveedor = None
        if proveedor_id is not None:
            try:
                proveedor = Proveedor.objects.get(id=proveedor_id)
            except Proveedor.DoesNotExist:
                raise ValidationError({'proveedor_id': 'Proveedor no encontrado.'})

        producto = Producto.objects.create(
            codigo=data['codigo'],
            nombre=data['nombre'],
            categoria=categoria,
            proveedor=proveedor,  # Puede ser None
            stock_Inventario=data['stock_Inventario'],
            stock_Minimo=data['stock_Minimo'],
            precio_proveedor=data['precio_proveedor'],
            precio_tienda=data['precio_tienda'],
            ganancia_porcentaje=data.get('ganancia_porcentaje'),
            ganancia_pesos=data.get('ganancia_pesos'),
        )

        return producto