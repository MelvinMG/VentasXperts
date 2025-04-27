package com.app.ventasxpertsmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.ventasxpertsmobile.ui.usuarios.UsuariosScreen
import com.app.ventasxpertsmobile.ui.usuarios.DetalleUsuarioScreen

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
                }
            )
        }
        composable(
            "detalle_usuario/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            DetalleUsuarioScreen(
                userId = userId,
                onLogout = onLogout,
                onNavigationSelected = { route -> navController.navigate(route) }
            )
        }

        // Más pantallas aquí...
    }
}
