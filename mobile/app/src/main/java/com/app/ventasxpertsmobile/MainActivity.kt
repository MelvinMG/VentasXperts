package com.app.ventasxpertsmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.ventasxpertsmobile.ui.theme.VentasXpertsMobileTheme
import com.app.ventasxpertsmobile.ui.login.LoginScreen
import androidx.navigation.compose.rememberNavController
import com.app.ventasxpertsmobile.ui.navigation.AppNavHost
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.app.ventasxpertsmobile.ui.navigation.NavDrawer
import androidx.navigation.compose.currentBackStackEntryAsState
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RetrofitClient.initialize(applicationContext)
        setContent {
            VentasXpertsMobileTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                var initialRoute by remember { mutableStateOf<String?>(null) }
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""

                // Navega al route inicial solo si está logueado y route existe
                LaunchedEffect(isLoggedIn, initialRoute) {
                    if (isLoggedIn && initialRoute != null) {
                        navController.navigate(initialRoute!!) {
                            // popUpTo una ruta válida dentro del grafo (ej. "usuarios")
                            popUpTo("usuarios") { inclusive = true }
                        }
                        initialRoute = null
                    }
                }

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = { route ->
                            isLoggedIn = true
                            initialRoute = route // Almacena la ruta para navegar en LaunchedEffect
                        }
                    )
                } else {
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            NavDrawer(
                                selectedRoute = currentRoute,
                                onItemSelected = { route ->
                                    if (route != currentRoute) {
                                        navController.navigate(route) {
                                            popUpTo("usuarios") { saveState = true }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    scope.launch { drawerState.close() }
                                },
                                onLogout = {
                                    isLoggedIn = false
                                    navController.navigate("usuarios") {
                                        popUpTo("usuarios") { inclusive = true }
                                    }
                                },
                                onClose = { scope.launch { drawerState.close() } }
                            )
                        }
                    ) {
                        AppNavHost(
                            navController = navController,
                            onLogout = { isLoggedIn = false }
                        )
                    }
                }
            }
        }
    }
}
