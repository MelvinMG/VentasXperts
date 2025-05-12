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
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route ?: ""

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }
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
                                            popUpTo(navController.graph.startDestinationId) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    scope.launch { drawerState.close() }
                                },
                                onLogout = { isLoggedIn = false },
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
