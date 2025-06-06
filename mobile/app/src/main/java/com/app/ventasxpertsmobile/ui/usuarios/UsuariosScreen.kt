package com.app.ventasxpertsmobile.ui.usuarios

import com.app.ventasxpertsmobile.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.data.model.Usuarios
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Filter
import compose.icons.fontawesomeicons.solid.Users
import compose.icons.fontawesomeicons.solid.ThList
import compose.icons.fontawesomeicons.solid.ThLarge
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.UserPlus
import compose.icons.fontawesomeicons.solid.UserEdit
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import coil.compose.AsyncImage


@Composable
fun UsuariosScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    onVerDetalles: (Int) -> Unit,
    onAniadirUsuario: () -> Unit
) {
    val context = LocalContext.current

    var modoMosaico by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var filtroRol by remember { mutableStateOf<String?>(null) }
    var filtroOrden by remember { mutableStateOf<String?>(null) }
    var filtroVisible by remember { mutableStateOf(false) }
    var usuariosApi by remember { mutableStateOf<List<Usuarios>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getUsuarios().execute()
            }
            if (response.isSuccessful) {
                usuariosApi = response.body() ?: emptyList()
            } else {
                errorMsg = "Error servidor: ${response.code()}"
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            errorMsg = e.message ?: "Error desconocido"
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    val usuariosFiltrados = usuariosApi.filter {
        val nombre = it.nombreCompleto
        val rol = it.rol
        (query.isBlank() || nombre.contains(query, ignoreCase = true)) &&
                (filtroRol == null || rol == filtroRol)
    }

    BaseScreen(
        title = "Gestión de usuarios",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            // Barra de búsqueda + botones
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = {
                        Icon(
                            FontAwesomeIcons.Solid.Search, null,
                            modifier = Modifier.size(20.dp),
                            tint = AzulPrincipal
                        )
                    },
                    placeholder = {
                        Text(
                            "Buscar usuarios",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulPrincipal,
                        unfocusedBorderColor = Gris2,
                        cursorColor = AzulPrincipal
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextoInput)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = { onAniadirUsuario() }) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.UserPlus,
                        contentDescription = "Agregar usuario",
                        tint = AzulPrincipal,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(2.dp))
                // Filtro
                Box {
                    IconButton(onClick = { filtroVisible = true }) {
                        Icon(
                            FontAwesomeIcons.Solid.Filter,
                            contentDescription = "Filtro",
                            tint = AzulPrincipal,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = filtroVisible,
                        onDismissRequest = { filtroVisible = false }
                    ) {
                        Text(
                            "Filtrar por ROL",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        DropdownMenuItem(
                            text = { Text("Todos") },
                            onClick = { filtroRol = null; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Administrador") },
                            onClick = { filtroRol = "Administrador"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Gerente") },
                            onClick = { filtroRol = "Gerente"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Cajero") },
                            onClick = { filtroRol = "Cajero"; filtroVisible = false }
                        )
                        HorizontalDivider(color = Gris2)
                        Text(
                            "Ordenar por",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        DropdownMenuItem(
                            text = { Text("Recientes") },
                            onClick = { filtroOrden = "Recientes"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Antiguos") },
                            onClick = { filtroOrden = "Antiguos"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("A-Z") },
                            onClick = { filtroOrden = "A-Z"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Z-A") },
                            onClick = { filtroOrden = "Z-A"; filtroVisible = false }
                        )
                    }
                }
                Spacer(Modifier.width(2.dp))
                // Cambiar modo lista/mosaico
                IconButton(onClick = { modoMosaico = !modoMosaico }) {
                    Icon(
                        if (modoMosaico) FontAwesomeIcons.Solid.ThList else FontAwesomeIcons.Solid.ThLarge,
                        contentDescription = "Cambiar vista",
                        tint = AzulPrincipal,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (modoMosaico) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(4.dp)
                    ) {
                        items(usuariosFiltrados.size) { idx ->
                            UsuarioCardMosaico(usuario = usuariosFiltrados[idx], onVerDetalles = onVerDetalles)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(usuariosFiltrados) { usuario ->
                            UsuarioCardLista(usuario, onVerDetalles)
                        }
                    }
                }
            }
        }
    }
}

// --- COMPONENTES TARJETA ---

@Composable
fun UsuarioCardLista(usuario: Usuarios, onVerDetalles: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 6.dp, end = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco1),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    usuario.nombreCompleto,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    color = AzulPrincipal
                )
                Text(
                    usuario.rol,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gris1
                )
                Text(
                    usuario.fecha.ifEmpty { "Fecha no disponible" },
                    style = MaterialTheme.typography.bodySmall,
                    color = Gris1
                )
            }
            Button(
                onClick = { onVerDetalles(usuario.id) },
                colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Ver Detalles", style = MaterialTheme.typography.bodyMedium, color = Blanco1)
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = FontAwesomeIcons.Solid.UserEdit,
                    contentDescription = "Ver Detalles",
                    modifier = Modifier.size(18.dp),
                    tint = Blanco1
                )
            }
        }
        HorizontalDivider(
            color = Gris2,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


@Composable
fun UsuarioCardMosaico(usuario: Usuarios, onVerDetalles: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(156.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco1)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize()
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
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_default_user), // Recurso por defecto local
                        error = painterResource(id = R.drawable.ic_default_user)
                    )
                } else {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.Users,
                        contentDescription = null,
                        tint = AzulPrincipal,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        usuario.nombreCompleto,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 12.sp)
                    )
                    Text(
                        usuario.rol,
                        color = Gris1,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                    )
                    Text(
                        usuario.fecha.ifEmpty { "Fecha no disponible" },
                        style = MaterialTheme.typography.bodySmall,
                        color = Gris1
                    )

                }
                Button(
                    onClick = { onVerDetalles(usuario.id) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = FontAwesomeIcons.Solid.UserEdit,
                        contentDescription = "Ver Detalles",
                        tint = Blanco1,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewUsuariosScreen() {
    UsuariosScreen(onVerDetalles = {}, onAniadirUsuario = {})
}
