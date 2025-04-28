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
import com.app.ventasxpertsmobile.ui.bitacora.BitacoraScreen
import com.app.ventasxpertsmobile.ui.inventario.InventarioScreen
import com.app.ventasxpertsmobile.ui.inventario.Producto
import com.app.ventasxpertsmobile.ui.inventario.EditarProductoScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "usuarios"
    ) {
        composable("usuarios") {
            UsuariosScreen(
                onLogout = onLogout,
                onNavigationSelected = { route ->
                    navController.navigate(route)
                },
                onVerDetalles = { userId ->
                    navController.navigate("detalle_usuario/$userId")
                },
                onAniadirUsuario = {
                    navController.navigate("aniadir_usuario")
                }
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
        composable("bitacora") {
            BitacoraScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        // INVENTARIO
        composable("inventario") {
            val productos = listOf(
                Producto(1, "Coca-Cola", 18.00, "Suficiente stock", "Refrescos"),
                Producto(2, "Fanta", 17.00, "Mínimo de stock", "Refrescos"),
                Producto(3, "Corn Flakes", 42.00, "Sin stock", "Cereal"),
                Producto(4, "Yogur", 25.00, "Suficiente stock", "Lácteos"),
                Producto(5, "Cheetos", 14.00, "Suficiente stock", "Botanas")
            )
            InventarioScreen(
                productos = productos,
                onAgregar = {
                    navController.navigate("editar_producto/Nuevo%20producto")
                },
                onEditar = { producto ->
                    navController.navigate("editar_producto/${producto.nombre.replace(" ", "%20")}")
                },
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        // Pantalla para crear/editar producto (usa el mismo Composable)
        composable(
            "editar_producto/{nombreProducto}",
            arguments = listOf(navArgument("nombreProducto") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombreProducto = backStackEntry.arguments?.getString("nombreProducto") ?: "Nuevo producto"
            EditarProductoScreen(
                nombreProducto = nombreProducto,
                onCancel = { navController.popBackStack() },
                onCreate = { navController.popBackStack() }
            )
        }
        // Puedes agregar más pantallas aquí...
    }
}
