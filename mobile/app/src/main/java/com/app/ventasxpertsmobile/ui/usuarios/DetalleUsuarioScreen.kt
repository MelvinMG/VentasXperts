package com.app.ventasxpertsmobile.ui.usuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.Users

import androidx.compose.runtime.Composable

@Composable
fun DetalleUsuarioScreen(
    userId: Int?,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val usuario = usuariosDemo.find { it.id == userId }

    BaseScreen(
        title = "Detalles de usuario",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        if (usuario == null) {
            Box(
                Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Usuario no encontrado")
            }
            return@BaseScreen
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (usuario.foto != null) {
                    Image(
                        painter = painterResource(id = usuario.foto),
                        contentDescription = "Foto usuario",
                        modifier = Modifier.size(90.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Users,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp),
                        tint = AzulPrincipal
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(usuario.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
            Text("ejemplo@correo.com", modifier = Modifier.align(Alignment.CenterHorizontally))
            Text(usuario.rol, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { /* TODO: Cambiar rol */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
            ) {
                Text("Cambiar Rol")
            }

            Spacer(Modifier.height(12.dp))
            Text("Detalles de la cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Registrado en:\nDD-MM-AA HH:MM PM\nÚltimo acceso:\nDD-MM-AA HH:MM PM\nNombre:\nNombre Ap. Paterno Ap. Materno\nGénero: Masculino\nTeléfono: (+52) ##-###-####\nRFC: 1234567890\nCURP: 1234567890")

            Spacer(Modifier.height(16.dp))
            Text("Acciones de la cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            ActionRow("Editar usuario") { /* Navegar/Mostrar dialogo */ }
            ActionRow("Cambiar rol") { /* Navegar/Mostrar dialogo */ }
            ActionRow("Eliminar usuario") { /* Navegar/Mostrar dialogo */ }
        }
    }
}

// Reusable Row
@Composable
fun ActionRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = AzulPrincipal // Opcional para tu tema
        )
        Icon(
            FontAwesomeIcons.Solid.ArrowRight,
            contentDescription = null,
            tint = AzulPrincipal,
            modifier = Modifier.size(28.dp) // <-- Ajusta aquí el tamaño. Prueba con 24.dp o 20.dp si sigue grande
        )
    }
}

