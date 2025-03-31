
# Bienvenido a la seccion Inventario

En esta sección se manejan distintas solicitudes http que permiten simular el proceso de la creacion de un producto en el inventario. Las peticiones se ven a continuacion paso a paso:

## 📌 Requisitos

Asegúrate de tener instalado lo siguiente antes de comenzar:

-   **Extension de VSC: Thunder Client**
-   **Crear un superusuario usando python manage.py createsuperuser** 

## ⚙️ Pasos para completar un cobro de caja

### 1.- Crea un usuario (si aún no existe)

# Crea usuario a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/users/create_user/
# En auth basica pon las credenciales del super usuario que creaste

# JSON body

{
  "user": {
    "username": "gerente1",
    "email": "gerente@gmail.com",
    "password": "12345",
    "first_name": "Luis",
    "last_name": "Hernandez",
    "group_id": 0  // Este campo se ignora; el rol se asigna en el siguiente paso
  },
  "persona": {
    "nombre": "Luis",
    "apPaterno": "Hernandez",
    "apMaterno": "Lopez",
    "genero": "M",
    "correo": "gerente@gmail.com",
    "telefono": "5511223344",
    "rfc": "HELU900101",
    "curp": "HELU900101HDFRZN01"
  }
}

# 3. Asignar el rol "Gerente" al usuario
http://127.0.0.1:8000/api/users/assign_role_to_user/

# Json body
{
  "username": "gerente1",
  "group_name": "Gerente"
}

# Creamos un producto con el usuario creado [POST]
http://127.0.0.1:8000/api/catalogo/productos/crear_producto/

# Json ejemplo

{
  "codigo": "P00123",
  "nombre": "Cereal ChocoCrispis 600g",
  "categoria_nombre": "Desayunos",
  "proveedor_nombre": "Distribuidora Kelloggs",
  "proveedor_telefono": "5544332211",
  "proveedor_correo": "ventas@kelloggs.com",
  "stock_Inventario": 120,
  "stock_Minimo": 10,
  "precio_proveedor": 35.00,
  "precio_tienda": 45.00
}

# Si queremos modificar el producto [PUT]
http://127.0.0.1:8000/api/catalogo/productos/1/modificar/

# Json ejemplo

{
  "nombre": "Cereal ChocoCrispis 750g",
  "stock_Inventario": 150,
  "precio_proveedor": 36.00,
  "precio_tienda": 46.00
}

# Si se quiere eliminar se utiliza el id del producto para eliminar  [DELETE]
http://127.0.0.1:8000/api/catalogo/productos/2/eliminar/

# Si se quiere buscar el producto por nombre  si es cerial se puede buscar con C [GET]
 http://127.0.0.1:8000/api/catalogo/productos/buscar_filtrar/?nombre=cereal

# Filtrado por estado de stock suficiente,carente,agotado [GET]
 http://127.0.0.1:8000/api/catalogo/productos/buscar_filtrar/?estado=suficiente
 
# Informacion de stock general [GET]
http://127.0.0.1:8000/api/catalogo/productos/reporte_stock/

# Crear informacion del reporte en pdf en proceso (no terminado ) [GET]
http://127.0.0.1:8000/api/catalogo/productos/reporte_stock_pdf/
