package com.app.ventasxpertsmobile.ui.navigation

sealed class NavigationItem(val route: String, val label: String) {
    //object Bitacora : NavigationItem("bitacora", "Bitácora")
    object Usuarios : NavigationItem("usuarios", "Usuarios")
    object Login : NavigationItem("login", "Login")
    object Logout : NavigationItem("logout", "Cerrar sesión")
}