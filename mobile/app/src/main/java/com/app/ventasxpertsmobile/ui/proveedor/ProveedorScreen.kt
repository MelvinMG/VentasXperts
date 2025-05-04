package com.app.ventasxpertsmobile.ui.proveedor

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.layout.ContentScale

import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import com.app.ventasxpertsmobile.ui.theme.Blanco1
import com.app.ventasxpertsmobile.ui.theme.Gris2
import com.app.ventasxpertsmobile.ui.theme.TextoInput

import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Filter
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.ArrowRight
import com.app.ventasxpertsmobile.R

// Demo data class
data class Proveedor(
    val nombre: String,
    val descripcion: String,
    val telefono: String,
    val categoria: String,
    val correo: String,
    val imagenRes: Int,
    val logoRes: Int
)

// Datos demo
val proveedoresDemo = listOf(
    Proveedor(
        "Coca-Cola",
        "Distribuidora de la familia Coca-Cola",
        "Teléfono",
        "Correo",
        "Refresco",  // Nueva propiedad 'categoria'
        R.drawable.cocacola,
        R.drawable.cocacola
    ),
    Proveedor(
        "Pepsi",
        "Distribuidora de la familia Pepsi Cola",
        "Teléfono",
        "Correo",
        "Refresco",  // Nueva propiedad 'categoria'
        R.drawable.pepsi,
        R.drawable.pepsi
    ),
    Proveedor(
        "BIMBO",
        "Distribuidora de productos de panadería y confitería",
        "Teléfono",
        "Correo",
        "Reposteria",  // Nueva propiedad 'categoria'
        R.drawable.bimbo,
        R.drawable.bimbo
    )
)


@Composable
fun ProveedorScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    onVerDetalles: (Int) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var filtroRol by remember { mutableStateOf<String?>(null) }
    var filtroVisible by remember { mutableStateOf(false) }

    val proveedores = proveedoresDemo.filter {
        (query.isBlank() || it.nombre.contains(query, ignoreCase = true)) &&
                (filtroRol == null || it.categoria == filtroRol)
    }

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
            // Barra de búsqueda
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
                            "Buscar proveedores",
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
                        // Filtro de categoría (Rol)
                        DropdownMenuItem(
                            text = { Text("Todos") },
                            onClick = { filtroRol = null; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Coca-Cola") },
                            onClick = { filtroRol = "Coca-Cola"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("Pepsi") },
                            onClick = { filtroRol = "Pepsi"; filtroVisible = false }
                        )
                        DropdownMenuItem(
                            text = { Text("BIMBO") },
                            onClick = { filtroRol = "BIMBO"; filtroVisible = false }
                        )
                    }
                }
            }

            // Aquí mostramos las tarjetas de los proveedores
            LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
                items(proveedores) { proveedor ->
                    ProveedorCard(proveedor = proveedor, onVerDetalles = onVerDetalles)
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
        colors = CardDefaults.cardColors(containerColor = Blanco1)
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Image(
                painter = painterResource(id = proveedor.imagenRes),
                contentDescription = "Imagen de ${proveedor.nombre}",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                contentScale  = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = proveedor.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = proveedor.descripcion,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { onVerDetalles(proveedor.nombre.hashCode()) }) {
                Icon(
                    FontAwesomeIcons.Solid.ArrowRight,
                    contentDescription = "Ver detalles",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

// ---- COMPONENTES ----


@Preview(showBackground = true)
@Composable
fun PreviewProveedorScreen() {
    ProveedorScreen(onVerDetalles = {})
}