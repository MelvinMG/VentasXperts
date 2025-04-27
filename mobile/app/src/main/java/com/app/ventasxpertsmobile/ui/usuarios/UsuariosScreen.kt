package com.app.ventasxpertsmobile.ui.usuarios

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun UsuariosScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit={}
) {
    BaseScreen(
        title = "Gestión de usuarios",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Aquí va el contenido de la pantalla de usuarios
            Text(
                text = "Aquí van los usuarios..."
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUsuariosScreen() {
    UsuariosScreen()
}
