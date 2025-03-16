
# Bienvenido a la seccion CAJA

En esta sección se manejan distintas solicitudes http que permiten simular el proceso de un cobro de un carrito para una caja de una tienda. Las peticiones se ven a continuacion paso a paso:

----------

## 📌 Requisitos

Asegúrate de tener instalado lo siguiente antes de comenzar:

-   **Extension de VSC: Thunder Client**

## ⚙️ Pasos para completar un cobro de caja

### 1.- Crea un usuario (si aún no existe)

```bash
# Crea usuario a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/usuarios/crear/

# JSON body
{
  "username": "tu usuario",
  "email": "tuusuario@gmail.com",
  "password": "tu contraseña"
}
```

### 2.- Crea una caja (Si aún no existe)

```bash
# Crea una caja a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/cajas/

# JSON body
{
  "user":"1",
  "saldo_actual":"0.0",
  "corte_caja":"0.0"
}
```

### 3.- Crea un carrito (Si aún no existe)

```bash
# Crea un carrito a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/carrito/

# JSON body
{
  "precio_total": "0.0"
}
```

### 4.- Crea un proveedor y categoria (Si aún no existen)

```bash
# Crea un proveedor a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/proveedores/

# JSON body
{
  "nombre": "Almacenes Juanitas",
  "telefono": "4431022680",
  "correo": "juanita123@gmail.com"
}

# Crea una categoria a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/categorias/

# JSON body
{
  "nombre":"Frutas y Verduras"
}
```

### 5.- Crear un producto

```bash
# Crea un producto a traves de Thunder Client [POST]
http://127.0.0.1:8000/api/caja/productos/

# JSON body (Relacionalo con el id de la categoria y provedor creados anteriormente)
{
  "codigo": "4293728",
  "nombre": "Pepsi 3lts",
  "categoria": "1",
  "proveedor": "1",
  "stock_Inventario": "500",
  "stock_Minimo": "10",
  "precio_proveedor": "35.0",
  "precio_tienda": "40.0",
  "ganancia_pesos": "5.0"
}
```

### 6.- Crear un venta a traves de carrito producto

```bash
# Enlista todos los productos del carrito [GET]
http://127.0.0.1:8000/api/caja/carritoProducto/

# Agrega un producto al carrito a traves de Thunder Client [POST] (1 corresponde al id del producto)
http://127.0.0.1:8000/api/caja/carritoProducto/1/agregar/

# JSON content de agregar
{
  "cantidad": "1",
  "subtotal": "0.0",
  "carrito": "1",
  "producto": "1"
}

# Resta un producto al carrito a traves de Thunder Client [POST] (1 corresponde al id del producto)
http://127.0.0.1:8000/api/caja/carritoProducto/1/restar/

# Quita un producto por completo del carrito a traves de Thunder Client [POST] (1 corresponde al id del producto)
http://127.0.0.1:8000/api/caja/carritoProducto/1/quitar/

```

### 7.- Genera y descarga un ticket
##### Para este paso es importante autenticarte a traves de Thunder client.
- Ingresa a la parte de Auth
- En Basic ingresa las credenciales de usuario que creaste anteriomente.
- Con esto en el ticket se muestra el usuario que la generó.
```bash
# Enlista todos los tickets generados [GET]
http://127.0.0.1:8000/api/caja/tickets/historial/

# Genera un ticket de la venta actual [POST]
http://127.0.0.1:8000/api/caja/tickets/generar_ticket/

# Descarga un ticket [GET]
http://127.0.0.1:8000/api/caja/tickets/Nombre_ticket_sin_extension/descargar/
```

### 8.- Procesar venta
##### Para este paso es importante autenticarte a traves de Thunder client.
- Ingresa a la parte de Auth
- En Basic ingresa las credenciales de usuario que creaste anteriomente.
- Con esto en la venta se muestra el usuario que la generó.
```bash
# Con esto se procesa la venta y el carrito se vacía [POST]
http://127.0.0.1:8000/api/caja/ventas/procesar_venta/

```
----
## Siguiendo estos pasos ya habras completado una venta de forma satisfactoria a traves de solicitudes http. :)
