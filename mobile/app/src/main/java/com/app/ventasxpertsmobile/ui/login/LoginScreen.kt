package com.app.ventasxpertsmobile.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.ui.theme.Blanco1
import androidx.compose.ui.tooling.preview.Preview
@Composable
fun LoginScreen(onLoginClick: (String, String) -> Unit = { _, _ -> }) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val offsetValue = screenHeight * 0.25f // 25% de la pantalla (ajusta este valor a tu gusto)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blanco1)
    ) {
        // Header azul
        LoginHeader()

        // Formulario centrado y movido usando porcentaje
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .offset(y = offsetValue)
                .align(Alignment.TopCenter)
        ) {
            LoginForm(onLoginClick = onLoginClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen()
}
