package com.app.ventasxpertsmobile.ui.proveedor

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.data.api.ProveedorResponse
import com.app.ventasxpertsmobile.data.model.Proveedor
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.Filter
import compose.icons.fontawesomeicons.solid.Search
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProveedorScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    onVerDetalles: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filtroVisible by remember { mutableStateOf(false) }
    var filtroCategoria by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    var proveedorApi by remember { mutableStateOf<List<Proveedor>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getProveedor().execute()
            }
            if (response.isSuccessful) {
                val proveedoresResponse = response.body()
                proveedorApi = proveedoresResponse?.results ?: emptyList()
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

    // Filtrado simple por nombre y categoria
    val proveedoresFiltrados = proveedorApi.filter {
        val cumpleQuery = query.isBlank() || it.nombre.contains(query, ignoreCase = true)
        val cumpleFiltro = filtroCategoria == null || it.categoria == filtroCategoria
        cumpleQuery && cumpleFiltro
    }

    // Obtener categorías únicas para mostrar en filtro
    val categorias = proveedorApi.mapNotNull { it.categoria }.distinct()

    BaseScreen(
        title = "Gestión de proveedores",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp)
        ) {
            // Barra de búsqueda y filtro
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
                            FontAwesomeIcons.Solid.Search,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = AzulPrincipal
                        )
                    },
                    placeholder = {
                        Text("Buscar proveedores", style = MaterialTheme.typography.bodyMedium)
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
                Box {
                    IconButton(onClick = { filtroVisible = true }) {
                        Icon(
                            FontAwesomeIcons.Solid.Filter,
                            contentDescription = "Filtro",
                            tint = AzulPrincipal,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    DropdownMenu(expanded = filtroVisible, onDismissRequest = { filtroVisible = false }) {
                        DropdownMenuItem(
                            text = { Text("Todas las categorías") },
                            onClick = {
                                filtroCategoria = null
                                filtroVisible = false
                            }
                        )
                        categorias.forEach { categoria ->
                            DropdownMenuItem(
                                text = { Text(categoria) },
                                onClick = {
                                    filtroCategoria = categoria
                                    filtroVisible = false
                                }
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                    items(proveedoresFiltrados) { proveedor ->
                        ProveedorCard(proveedor = proveedor, onVerDetalles = onVerDetalles)
                    }
                }
            }
        }
    }
}


@Composable
fun ProveedorCard(proveedor: Proveedor, onVerDetalles: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = RoundedCornerShape(8.dp),
        colors = cardColors(containerColor = Blanco1)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            // Imagen remota o placeholder (opcional)
            // Por ahora, solo iniciales en círculo o icono
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(AzulPrincipal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = proveedor.nombre.take(1).uppercase(),
                    style = MaterialTheme.typography.titleLarge.copy(color = Blanco1)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = proveedor.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = proveedor.telefono,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = proveedor.correo,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onVerDetalles(proveedor.id) }) {
                Icon(
                    FontAwesomeIcons.Solid.ArrowRight,
                    contentDescription = "Ver detalles",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}