package com.app.ventasxpertsmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.app.ventasxpertsmobile.ui.theme.VentasXpertsMobileTheme
import com.app.ventasxpertsmobile.ui.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VentasXpertsMobileTheme {
                LoginScreen()
            }
        }
    }
}
