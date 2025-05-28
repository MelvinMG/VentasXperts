package com.app.ventasxpertsmobile.ui.usuarios

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.ventasxpertsmobile.data.model.Usuarios
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast

@Composable
fun DetalleUsuarioScreen(
    userId: Int,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    onUserDeleted: () -> Unit = {}
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var usuario by remember { mutableStateOf<Usuarios?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    // Carga usuario por ID desde API usando suspend
    LaunchedEffect(userId) {
        isLoading = true
        errorMsg = null
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getUsuarioById(userId)
            }
            if (response.isSuccessful) {
                usuario = response.body()
            } else {
                errorMsg = "Error ${response.code()}: ${response.message()}"
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            errorMsg = e.localizedMessage ?: "Error desconocido"
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    BaseScreen(
        title = "Detalles de usuario",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                errorMsg != null -> Text(text = errorMsg ?: "Error inesperado")
                usuario != null -> UsuarioDetalleContent(
                    usuario = usuario!!,
                    onEditUser = { onNavigationSelected("EditarUsuario/${usuario!!.id}") },
                    onChangeRole = { onNavigationSelected("CambiarRol/${usuario!!.id}") },
                    onDeleteUserClicked = { showDeleteConfirm = true }
                )
                else -> Text("Usuario no encontrado")
            }
        }

        if (showDeleteConfirm && usuario != null) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro que quieres eliminar este usuario?") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteConfirm = false
                        scope.launch {
                            try {
                                val response = RetrofitClient.api.deleteUser(usuario!!.id)
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
fun UsuarioDetalleContent(
    usuario: Usuarios,
    onEditUser: () -> Unit,
    onChangeRole: () -> Unit,
    onDeleteUserClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val fotoUrl = usuario.persona?.fotoUrl
            if (!fotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = fotoUrl,
                    contentDescription = "Foto usuario",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = com.app.ventasxpertsmobile.R.drawable.ic_default_user),
                    error = painterResource(id = com.app.ventasxpertsmobile.R.drawable.ic_default_user)
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
            usuario.nombreCompleto,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(usuario.email.ifEmpty { "Sin correo" }, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(usuario.rol, modifier = Modifier.align(Alignment.CenterHorizontally))

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onChangeRole,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
        ) {
            Text("Cambiar Rol")
        }

        Spacer(Modifier.height(12.dp))

        Text("Detalles de la cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Registrado en:\n${usuario.fecha.ifEmpty { "Fecha no disponible" }}")
        Text("Nombre completo:\n${usuario.nombreCompleto}")
        Text("Género: ${usuario.persona?.genero ?: "No especificado"}")
        Text("Teléfono: ${usuario.persona?.telefono ?: "No especificado"}")
        Text("RFC: ${usuario.persona?.rfc ?: "No especificado"}")
        Text("CURP: ${usuario.persona?.curp ?: "No especificado"}")

        Spacer(Modifier.height(16.dp))

        Text("Acciones de la cuenta", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        ActionRow("Editar usuario", onEditUser)
        ActionRow("Cambiar rol", onChangeRole)
        ActionRow("Eliminar usuario", onDeleteUserClicked)
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
