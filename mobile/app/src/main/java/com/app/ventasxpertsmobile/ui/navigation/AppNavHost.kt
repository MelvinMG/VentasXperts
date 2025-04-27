package com.app.ventasxpertsmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.ventasxpertsmobile.ui.usuarios.UsuariosScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLogout: () -> Unit // Recibe un callback para cerrar sesión
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
                }
            )
        }

        // Aquí puedes agregar tus otras pantallas, por ejemplo Bitacora, Inventario, etc.
    }
}
