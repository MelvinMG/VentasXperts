package com.app.ventasxpertsmobile.ui.usuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.Users
import kotlinx.coroutines.launch

@Composable
fun DetalleUsuarioScreen(
    userId: Int?, // Opcional para test
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    onUserDeleted: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val api = RetrofitClient.api

    // Para test, usuario fijo. Cambia si quieres usar API real
    val usuario = usuariosDemo.find { it.id == 7 }

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

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
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape),
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
            Text(
                usuario.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
            Text(
                "Registrado en:\nDD-MM-AA HH:MM PM\nÚltimo acceso:\nDD-MM-AA HH:MM PM\nNombre:\nNombre Ap. Paterno Ap. Materno\nGénero: Masculino\nTeléfono: (+52) ##-###-####\nRFC: 1234567890\nCURP: 1234567890"
            )

            Spacer(Modifier.height(16.dp))
            Text("Acciones de la cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)

            ActionRow("Editar usuario") {
                onNavigationSelected("EditarUsuario/7") // ID fijo para testeo
            }
            ActionRow("Cambiar rol") {
                onNavigationSelected("CambiarRol/${usuario.id}")
            }
            ActionRow("Eliminar usuario") {
                showDeleteConfirm = true
            }
        }

        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro que quieres eliminar este usuario?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteConfirm = false
                        scope.launch {
                            try {
                                val response = api.deleteUser(usuario.id)
                                if (response.isSuccessful) {
                                    resultMessage = "Usuario eliminado correctamente"
                                    onUserDeleted()
                                } else {
                                    resultMessage = "Error al eliminar usuario: ${response.code()}"
                                }
                            } catch (e: Exception) {
                                resultMessage = "Error de red: ${e.localizedMessage ?: e.message}"
                            }
                            showResultDialog = true
                        }
                    }) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }

        if (showResultDialog) {
            AlertDialog(
                onDismissRequest = { showResultDialog = false },
                title = { Text("Resultado") },
                text = { Text(resultMessage) },
                confirmButton = {
                    TextButton(onClick = { showResultDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

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
            color = AzulPrincipal
        )
        Icon(
            FontAwesomeIcons.Solid.ArrowRight,
            contentDescription = null,
            tint = AzulPrincipal,
            modifier = Modifier.size(28.dp)
        )
    }
}
