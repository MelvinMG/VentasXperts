package com.app.ventasxpertsmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.ventasxpertsmobile.ui.usuarios.UsuariosScreen
import com.app.ventasxpertsmobile.ui.usuarios.DetalleUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.CrearUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.EditarUsuarioScreen
import com.app.ventasxpertsmobile.ui.bitacora.BitacoraScreen
import com.app.ventasxpertsmobile.ui.caja.VentasScreen
import com.app.ventasxpertsmobile.ui.caja.TicketScreen
import com.app.ventasxpertsmobile.ui.caja.HistorialTicketsScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendasCatalogoScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendaProductosScreen
import com.app.ventasxpertsmobile.ui.inventario.InventarioScreen
import com.app.ventasxpertsmobile.ui.inventario.Producto
import com.app.ventasxpertsmobile.ui.inventario.EditarProductoScreen
import com.app.ventasxpertsmobile.ui.proveedor.ProveedorScreen
import com.app.ventasxpertsmobile.ui.proveedor.DetalleProveedorScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Usuarios.route
    ) {
        composable(NavigationItem.Usuarios.route) {
            UsuariosScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) },
                onVerDetalles = { userId -> navController.navigate("detalle_usuario/$userId") },
                onAniadirUsuario = { navController.navigate("aniadir_usuario") }
            )
        }
        composable(
            "detalle_usuario/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            DetalleUsuarioScreen(
                userId = userId,
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("aniadir_usuario") {
            CrearUsuarioScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(
            "EditarUsuario/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            EditarUsuarioScreen(
                userId = userId,
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(NavigationItem.Bitacora.route) {
            BitacoraScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(NavigationItem.Caja.route) {
            VentasScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("ticket") {
            TicketScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("historial") {
            HistorialTicketsScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("catalogo") {
            TiendasCatalogoScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("info_tienda") {
            TiendaProductosScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(NavigationItem.Proveedor.route) {
            ProveedorScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) },
                onVerDetalles = { proveedorId -> navController.navigate("detalle_proveedor/$proveedorId") }
            )
        }
        composable(
            "detalle_proveedor/{proveedorId}",
            arguments = listOf(navArgument("proveedorId") { type = NavType.IntType })
        ) { backStackEntry ->
            val proveedorId = backStackEntry.arguments?.getInt("proveedorId") ?: 0
            DetalleProveedorScreen(
                proveedorId = proveedorId,
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(NavigationItem.Inventario.route) {
            val productos = listOf(
                Producto(1, "Coca-Cola", 18.00, "Suficiente stock", "Refrescos"),
                Producto(2, "Fanta", 17.00, "Mínimo de stock", "Refrescos"),
                Producto(3, "Corn Flakes", 42.00, "Sin stock", "Cereal"),
                Producto(4, "Yogur", 25.00, "Suficiente stock", "Lácteos"),
                Producto(5, "Cheetos", 14.00, "Suficiente stock", "Botanas")
            )
            InventarioScreen(
                productos = productos,
                onAgregar = { navController.navigate("editar_producto/Nuevo%20producto") },
                onEditar = { producto -> navController.navigate("editar_producto/${producto.nombre.replace(" ", "%20")}") },
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable(
            route = "editar_producto/{nombreProducto}",
            arguments = listOf(navArgument("nombreProducto") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombreProducto = backStackEntry.arguments?.getString("nombreProducto") ?: "Nuevo producto"
            EditarProductoScreen(
                nombreProducto = nombreProducto,
                onCancel = { navController.popBackStack() },
                onCreate = { navController.popBackStack() },
                navController = navController
            )
        }
    }
}
