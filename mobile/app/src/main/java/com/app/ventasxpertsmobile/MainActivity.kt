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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VentasXpertsMobileTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                val navController = rememberNavController()

                if (!isLoggedIn) {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                } else {
                    AppNavHost(
                        navController = navController,
                        onLogout = { isLoggedIn = false }
                    )
                }
            }
        }
    }
}


