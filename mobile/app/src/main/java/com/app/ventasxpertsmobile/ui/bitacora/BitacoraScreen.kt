package com.app.ventasxpertsmobile.ui.bitacora

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.data.model.BitacoraEntry
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    // Dummy data
    val bitacoraList = listOf(
        BitacoraEntry(1, "Melvin Marín", "Administrador", "Eliminó usuario", "Eliminó usuario \"Pablo Eduardo\"", "22-04-25 15:26"),
        BitacoraEntry(2, "Melvin Marín", "Administrador", "Actualizó usuario", "Actualizó teléfono de \"Juan Pérez\"", "22-04-25 15:26"),
        BitacoraEntry(3, "Melvin Marín", "Administrador", "Inició sesión", "Inició sesión como \"melvin_admin\"", "21-04-25 09:10"),
        BitacoraEntry(4, "Juan Pérez", "Cajero", "Registró venta", "Vendió producto A", "21-04-25 10:05")
    )

    val fechaList = listOf("01/04/24", "02/04/24", "03/04/24")
    val accionesList = listOf("Eliminó usuario", "Actualizó usuario", "Inició sesión", "Registró venta")
    val usuariosList = listOf("Melvin Marín", "Juan Pérez", "Ana López")

    BaseScreen(
        title = "Bitácora",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
        ) {
            BitacoraFiltersButtons(
                fechaList = fechaList,
                accionesList = accionesList,
                usuariosList = usuariosList
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bitacoraList) { entry ->
                    BitacoraCard(entry)
                }
            }
        }
    }
}

@Composable
fun BitacoraFiltersButtons(
    fechaList: List<String>,
    accionesList: List<String>,
    usuariosList: List<String>
) {
    var selectedFecha by remember { mutableStateOf(fechaList.first()) }
    var expandedFecha by remember { mutableStateOf(false) }

    var selectedAccion by remember { mutableStateOf(accionesList.first()) }
    var expandedAccion by remember { mutableStateOf(false) }

    var selectedUsuario by remember { mutableStateOf(usuariosList.first()) }
    var expandedUsuario by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        BitacoraDropdown(
            selected = selectedFecha,
            expanded = expandedFecha,
            options = fechaList,
            onExpandedChange = { expandedFecha = it },
            onSelect = { selectedFecha = it; expandedFecha = false }
        )
        BitacoraDropdown(
            selected = selectedAccion,
            expanded = expandedAccion,
            options = accionesList,
            onExpandedChange = { expandedAccion = it },
            onSelect = { selectedAccion = it; expandedAccion = false }
        )
        BitacoraDropdown(
            selected = selectedUsuario,
            expanded = expandedUsuario,
            options = usuariosList,
            onExpandedChange = { expandedUsuario = it },
            onSelect = { selectedUsuario = it; expandedUsuario = false }
        )
    }
}

@Composable
fun BitacoraDropdown(
    selected: String,
    expanded: Boolean,
    options: List<String>,
    onExpandedChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Box {
        OutlinedButton(
            onClick = { onExpandedChange(true) },
            shape = MaterialTheme.shapes.medium,
            border = BorderStroke(1.dp, AzulPrincipal),
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Blanco1),
            modifier = Modifier.widthIn(min = 120.dp).height(44.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(selected, color = AzulPrincipal, style = MaterialTheme.typography.bodyMedium)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Desplegar",
                    tint = AzulPrincipal,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = { onSelect(option) }
                )
            }
        }
    }
}

@Composable
fun BitacoraCard(entry: BitacoraEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Blanco1),
        border = BorderStroke(1.dp, Gris2)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Usuario",
                    tint = AzulPrincipal,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(entry.usuario, fontWeight = FontWeight.Bold, color = AzulPrincipal)
                    Text(entry.rol, style = MaterialTheme.typography.bodySmall, color = Gris1)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("Acción: ${entry.accion}", color = Gris1, fontWeight = FontWeight.SemiBold)
            Text(entry.detalle, style = MaterialTheme.typography.bodyMedium, color = Gris1)
            Spacer(modifier = Modifier.height(3.dp))
            Text(entry.fecha, style = MaterialTheme.typography.bodySmall, color = Gris1)
        }
    }
}
