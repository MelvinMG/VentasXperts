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

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.data.model.Bitacora
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.*
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.withContext
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.app.ventasxpertsmobile.data.network.RetrofitClient


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BitacoraScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current

    // Estados para filtros
    var selectedFecha by remember { mutableStateOf<String?>(null) }  // "YYYY-MM" o null
    var selectedAccion by remember { mutableStateOf<String?>(null) }

    // Datos y carga
    var bitacoraList by remember { mutableStateOf<List<Bitacora>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Opciones filtro fechas (meses únicos)
    var fechaList by remember { mutableStateOf<List<String>>(listOf("Todas las fechas")) }
    // Lista fija de acciones (puedes hacerla dinámica)
    val accionesMap = mapOf(
        null to "Todas las acciones",
        "create" to "Crear",
        "update" to "Actualizar",
        "delete" to "Eliminar",
        "purchase" to "Compra",
        "login" to "Inicio de sesión",
        "logout" to "Cierre de sesión"
    )

    val accionesList = accionesMap.values.toList()
    // Carga inicial con LaunchedEffect para cargar bitácora y fechas únicas
    fun getAccionKeyByValue(value: String): String? {
        return accionesMap.entries.find { it.value == value }?.key
    }
    LaunchedEffect(selectedFecha, selectedAccion) {
        isLoading = true
        errorMsg = null
        try {
            val filtroFecha = selectedFecha?.takeIf { it != "Todas las fechas" }
            val filtroAccion = selectedAccion?.takeIf { it != "Todas las acciones" }?.lowercase()

            val call: retrofit2.Call<List<Bitacora>> = RetrofitClient.api.getBitacora(filtroFecha, filtroAccion)
            val response: retrofit2.Response<List<Bitacora>> = withContext(Dispatchers.IO) {
                call.execute()
            }

            if (response.isSuccessful) {
                bitacoraList = response.body() ?: emptyList()
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


    // Recargar bitacora cuando filtros cambien
    LaunchedEffect(selectedFecha, selectedAccion) {
        isLoading = true
        errorMsg = null
        try {
            val filtroFecha = selectedFecha?.takeIf { it != "Todas las fechas" }
            val filtroAccion = selectedAccion?.takeIf { it != "Todas las acciones" }?.lowercase()
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getBitacora(filtroFecha, filtroAccion).execute()
            }
            if (response.isSuccessful) {
                bitacoraList = response.body() ?: emptyList()
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
                selectedFecha = selectedFecha ?: "Todas las fechas",
                onFechaSelected = { selectedFecha = if (it == "Todas las fechas") null else it },
                accionesList = accionesList,
                selectedAccion = accionesMap[selectedAccion] ?: "Todas las acciones",
                onAccionSelected = { label ->
                    selectedAccion = getAccionKeyByValue(label)  }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if (bitacoraList.isEmpty()) {
                    Text(
                        "No hay registros en la bitácora",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = Gris1
                    )
                } else {
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
    }
}

@Composable
fun BitacoraFiltersButtons(
    fechaList: List<String>,
    selectedFecha: String,
    onFechaSelected: (String) -> Unit,
    accionesList: List<String>,
    selectedAccion: String,
    onAccionSelected: (String) -> Unit
) {
    var expandedFecha by remember { mutableStateOf(false) }
    var expandedAccion by remember { mutableStateOf(false) }

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
            onSelect = { onFechaSelected(it); expandedFecha = false }
        )
        BitacoraDropdown(
            selected = selectedAccion,
            expanded = expandedAccion,
            options = accionesList,
            onExpandedChange = { expandedAccion = it },
            onSelect = { onAccionSelected(it); expandedAccion = false }
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
            modifier = Modifier
                .widthIn(min = 140.dp)
                .height(44.dp)
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
fun BitacoraCard(entry: Bitacora) {
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
                    Text(
                        text = entry.usuario ?: "Desconocido",
                        fontWeight = FontWeight.Bold,
                        color = AzulPrincipal
                    )
                    Text(
                        text = entry.rol ?: "Sin rol",
                        style = MaterialTheme.typography.bodySmall,
                        color = Gris1
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Acción: ${entry.accion?.capitalize() ?: "N/A"}",
                color = Gris1,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = entry.detalle ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Gris1
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = entry.formattedFecha(),
                style = MaterialTheme.typography.bodySmall,
                color = Gris1
            )
        }
    }
}
