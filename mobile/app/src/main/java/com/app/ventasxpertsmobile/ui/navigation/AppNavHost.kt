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
import com.app.ventasxpertsmobile.ui.caja.VentasScreen
import com.app.ventasxpertsmobile.ui.caja.TicketScreen
import com.app.ventasxpertsmobile.ui.caja.HistorialTicketsScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendasCatalogoScreen
import com.app.ventasxpertsmobile.ui.catalogo.TiendaProductosScreen

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
                onNavigationSelected = { route -> navController.navigate(route)}
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
        composable("historial"){
            HistorialTicketsScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("catalogo"){
            TiendasCatalogoScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }
        composable("info_tienda"){
            TiendaProductosScreen(
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }

        // ...más pantallas
    }
}

