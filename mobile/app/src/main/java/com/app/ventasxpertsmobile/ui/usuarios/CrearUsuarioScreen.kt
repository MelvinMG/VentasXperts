package com.app.ventasxpertsmobile.ui.usuarios

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.data.model.CreateUserRequest
import com.app.ventasxpertsmobile.data.model.PersonasData
import com.app.ventasxpertsmobile.data.model.UsersData
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.AccountCircle


fun String.trimOrNull(): String? = this.trim().takeIf { it.isNotEmpty() }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuarioScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var apPaterno by remember { mutableStateOf("") }
    var apMaterno by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var rfc by remember { mutableStateOf("") }
    var curp by remember { mutableStateOf("") }

    val generos = listOf("Masculino", "Femenino", "Prefiero no decirlo", "Desconocido")
    var generoExpanded by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }

    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    BaseScreen(
        title = "Crear usuario",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(10.dp))

                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .clickable { galleryLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Icono de usuario",
                        modifier = Modifier
                            .size(90.dp)
                            .clickable { galleryLauncher.launch("image/*") },
                        tint = AzulPrincipal
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "Usuario",
                    color = AzulPrincipal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 2.dp, bottom = 1.dp)
                )
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Nombre de usuario") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (showPassword) "Ocultar" else "Mostrar"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                )

                Text(
                    "Persona",
                    color = AzulPrincipal,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 2.dp, bottom = 1.dp)
                )
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = apPaterno,
                    onValueChange = { apPaterno = it },
                    label = { Text("Apellido Paterno") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = apMaterno,
                    onValueChange = { apMaterno = it },
                    label = { Text("Apellido Materno") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = generoExpanded,
                    onExpandedChange = { generoExpanded = !generoExpanded }
                ) {
                    OutlinedTextField(
                        value = genero,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Género") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .padding(bottom = 6.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = generoExpanded,
                        onDismissRequest = { generoExpanded = false }
                    ) {
                        generos.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    genero = option
                                    generoExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = rfc,
                    onValueChange = { rfc = it },
                    label = { Text("RFC") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
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
                        if (username.isBlank() || password.isBlank() || correo.isBlank() ||
                            nombre.isBlank() || apPaterno.isBlank() || genero.isBlank()
                        ) {
                            scope.launch {
                                snackbarHostState.showSnackbar("⚠️ Por favor completa todos los campos obligatorios.")
                            }
                            return@Button
                        }

                        val api = RetrofitClient.api

                        val request = CreateUserRequest(
                            user = UsersData(
                                username = username.trim(),
                                email = correo.trim(),
                                password = password,
                                first_name = nombre.trim(),
                                last_name = apPaterno.trim()
                            ),
                            persona = PersonasData(
                                nombre = nombre.trim(),
                                apPaterno = apPaterno.trim(),
                                apMaterno = apMaterno.trimOrNull(),
                                genero = when (genero) {
                                    "Masculino" -> "M"
                                    "Femenino" -> "F"
                                    "Prefiero no decirlo" -> "N"
                                    else -> "D"
                                },
                                correo = correo.trim(),
                                telefono = telefono.trimOrNull(),
                                rfc = rfc.trimOrNull(),
                                curp = curp.trimOrNull()
                            )
                        )

                        scope.launch {
                            try {
                                val response = api.createUser(request)
                                if (response.isSuccessful) {
                                    snackbarHostState.showSnackbar("✅ Usuario creado correctamente")
                                    username = ""
                                    password = ""
                                    nombre = ""
                                    apPaterno = ""
                                    apMaterno = ""
                                    genero = ""
                                    correo = ""
                                    telefono = ""
                                    rfc = ""
                                    curp = ""
                                    imageUri = null
                                } else {
                                    val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                                    snackbarHostState.showSnackbar("❌ Error: $errorMsg")
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("⚠️ Error de red: ${e.localizedMessage}")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Crear", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(16.dp))
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCrearUsuarioScreen() {
    CrearUsuarioScreen()
}
