package com.app.ventasxpertsmobile.ui.navigation

sealed class NavigationItem(val route: String, val label: String) {
    object Bitacora : NavigationItem("bitacora", "Bitácora")
    object Usuarios : NavigationItem("usuarios", "Usuarios")
    object Caja : NavigationItem("caja", "Caja")
    object Proveedor : NavigationItem("Proveedor", "Proveedor")
    object Ticket : NavigationItem("ticket", "Ticket")
    object Historial : NavigationItem("historial", "Historial de tickets")
    object Catalogo : NavigationItem("catalogo", "Catálogo")
    object InfoTienda : NavigationItem("info_tienda", "Información de tienda")
    object Inventario : NavigationItem("inventario", "Inventario")
    object EditarProducto : NavigationItem("editar_producto", "Editar Producto")
    object Login : NavigationItem("login", "Login")
    object Logout : NavigationItem("logout", "Cerrar sesión")
}
