package com.app.ventasxpertsmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.ventasxpertsmobile.data.model.Tienda
import com.app.ventasxpertsmobile.ui.usuarios.UsuariosScreen
import com.app.ventasxpertsmobile.ui.usuarios.DetalleUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.CrearUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.EditarUsuarioScreen
import com.app.ventasxpertsmobile.data.model.Proveedor
import com.app.ventasxpertsmobile.ui.bitacora.BitacoraScreen
import com.app.ventasxpertsmobile.ui.caja.HistorialTicketsScreen
import com.app.ventasxpertsmobile.ui.caja.TicketScreen
import com.app.ventasxpertsmobile.ui.caja.VentasScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendaProductosScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendasCatalogoScreen
import com.app.ventasxpertsmobile.ui.inventario.EditarProductoScreen
import com.app.ventasxpertsmobile.ui.inventario.InventarioScreen
import com.app.ventasxpertsmobile.ui.inventario.Producto
import com.app.ventasxpertsmobile.ui.inventario.EditarProductoScreen
import com.app.ventasxpertsmobile.ui.inventario.ProductoViewModel
import com.app.ventasxpertsmobile.ui.proveedor.ProveedorScreen
import com.app.ventasxpertsmobile.ui.proveedor.DetalleProveedorScreen
import com.app.ventasxpertsmobile.ui.proveedor.ProveedorScreen
import com.app.ventasxpertsmobile.ui.usuarios.CrearUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.DetalleUsuarioScreen
import com.app.ventasxpertsmobile.ui.usuarios.UsuariosScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Usuarios.route
    ) {
        // Usuarios
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

        // Bitacora
        composable(NavigationItem.Bitacora.route) {
            BitacoraScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }

        // Caja
        composable(NavigationItem.Caja.route) {
            VentasScreen(
                onLogout = onLogout,
                ventasViewModel = viewModel(),
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("ticket") {
            TicketScreen(
                onLogout = onLogout,
                ventasViewModel = viewModel(),
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("historial") {
            HistorialTicketsScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }

        // Catalogo
        composable("catalogo") {
            TiendasCatalogoScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) },

                // Aquí pasas la función para que navegue con el id de la tienda
                onTiendaSelected = { tienda ->
                    navController.navigate("info_tienda/${tienda.id}")
                }
            )
        }

        composable(
            route = "info_tienda/{tiendaId}",
            arguments = listOf(navArgument("tiendaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val tiendaId = backStackEntry.arguments?.getInt("tiendaId") ?: 0

            // Pasamos el id al ViewModel para obtener la tienda
            TiendaProductosScreen(
                tiendaId = tiendaId,
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }

        // Proveedores


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

        // Inventario
        composable(NavigationItem.Inventario.route) {
            val productoViewModel = viewModel<ProductoViewModel>()

            InventarioScreen(
                viewModel = productoViewModel,
                onAgregar = { navController.navigate("editar_producto/Nuevo%20producto") },
                onEditar = { producto -> navController.navigate("editar_producto/${producto.nombre.replace(" ", "%20")}") },
                onEliminar = {},
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
                navController = navController
            )
        }
    }
}