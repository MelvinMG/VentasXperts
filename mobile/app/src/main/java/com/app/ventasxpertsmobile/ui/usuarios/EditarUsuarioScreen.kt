package com.app.ventasxpertsmobile.ui.usuarios

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.data.model.UpdateUserRequest
import com.app.ventasxpertsmobile.data.model.UserData
import com.app.ventasxpertsmobile.data.model.PersonaData
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarUsuarioScreen(
    userId: Int,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val api = RetrofitClient.api
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var apPaterno by remember { mutableStateOf("") }
    var apMaterno by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var rfc by remember { mutableStateOf("") }
    var curp by remember { mutableStateOf("") }

    var passwordActual by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showPasswordActual by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var errorLoading by remember { mutableStateOf<String?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

    LaunchedEffect(userId) {
        isLoading = true
        errorLoading = null
        try {
            val response = api.getUsuarioById(userId)
            if (response.isSuccessful) {
                response.body()?.let { user ->
                    username = user.username
                    correo = user.email
                    nombre = user.persona?.nombre ?: ""
                    apPaterno = user.persona?.apPaterno ?: ""
                    apMaterno = user.persona?.apMaterno ?: ""
                    genero = when (user.persona?.genero) {
                        "M" -> "Masculino"
                        "F" -> "Femenino"
                        "N" -> "Prefiero no decirlo"
                        else -> "Desconocido"
                    }
                    telefono = user.persona?.telefono ?: ""
                    rfc = user.persona?.rfc ?: ""
                    curp = user.persona?.curp ?: ""
                }
            } else {
                errorLoading = "Error al cargar usuario: ${response.code()}"
            }
        } catch (e: Exception) {
            errorLoading = "Error de red: ${e.localizedMessage}"
        }
        isLoading = false
    }

    BaseScreen(
        title = "Editar usuario",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                errorLoading != null -> {
                    Text(
                        text = errorLoading ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.perfil),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(Modifier.height(12.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Nombre de usuario") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = passwordActual,
                            onValueChange = { passwordActual = it },
                            label = { Text("Contraseña actual (dejar en blanco si no cambia)") },
                            singleLine = true,
                            visualTransformation = if (showPasswordActual) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showPasswordActual = !showPasswordActual }) {
                                    Icon(
                                        imageVector = if (showPasswordActual) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (showPasswordActual) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = { Text("Nueva contraseña") },
                            singleLine = true,
                            visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                    Icon(
                                        imageVector = if (showNewPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                        contentDescription = if (showNewPassword) "Ocultar" else "Mostrar"
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        )

                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = apPaterno,
                            onValueChange = { apPaterno = it },
                            label = { Text("Apellido Paterno") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = apMaterno,
                            onValueChange = { apMaterno = it },
                            label = { Text("Apellido Materno") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = genero,
                            onValueChange = { genero = it },
                            label = { Text("Género") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = correo,
                            onValueChange = { correo = it },
                            label = { Text("Correo") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = telefono,
                            onValueChange = { telefono = it },
                            label = { Text("Teléfono") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = rfc,
                            onValueChange = { rfc = it },
                            label = { Text("RFC") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = curp,
                            onValueChange = { curp = it },
                            label = { Text("CURP") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                        )

                        Button(
                            onClick = {
                                if (username.isBlank() || correo.isBlank() || nombre.isBlank() || apPaterno.isBlank() || genero.isBlank()) {
                                    dialogMessage = "⚠️ Por favor completa todos los campos obligatorios."
                                    showDialog = true
                                    return@Button
                                }

                                val request = UpdateUserRequest(
                                    user = UserData(
                                        username = username,
                                        email = correo,
                                        password = null
                                    ),
                                    persona = PersonaData(
                                        nombre = nombre,
                                        apPaterno = apPaterno,
                                        apMaterno = apMaterno,
                                        genero = when (genero) {
                                            "Masculino" -> "M"
                                            "Femenino" -> "F"
                                            "Prefiero no decirlo" -> "N"
                                            else -> "D"
                                        },
                                        correo = correo,
                                        telefono = telefono,
                                        rfc = rfc,
                                        curp = curp
                                    ),
                                    password_actual = if (passwordActual.isBlank()) null else passwordActual,
                                    new_password = if (newPassword.isBlank()) null else newPassword
                                )

                                scope.launch {
                                    try {
                                        val response = api.updateUser(userId, request)
                                        if (response.isSuccessful) {
                                            successMessage = "✅ Usuario actualizado correctamente."
                                            showSuccessDialog = true
                                        } else {
                                            dialogMessage = "❌ Error: ${response.errorBody()?.string()}"
                                            showDialog = true
                                        }
                                    } catch (e: Exception) {
                                        dialogMessage = "⚠️ Error de red: ${e.localizedMessage}"
                                        showDialog = true
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text(
                                "Guardar",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Error") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text(
                    text = "Éxito",
                    color = AzulPrincipal,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = successMessage,
                    color = AzulPrincipal,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = AzulPrincipal
                    )
                ) {
                    Text("OK", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
