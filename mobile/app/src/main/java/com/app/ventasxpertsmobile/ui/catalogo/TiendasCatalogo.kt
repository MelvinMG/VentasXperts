package com.app.ventasxpertsmobile.ui.catalogo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendasCatalogoScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val tiendas = listOf(
        Tienda("Tienda 1", "Descripción simple de la tienda"),
        Tienda("Tienda 2", "Descripción simple de la tienda"),
        Tienda("Tienda 3", "Descripción simple de la tienda"),
        Tienda("Tienda 4", "Descripción simple de la tienda"),
        Tienda("Tienda 5", "Descripción simple de la tienda"),
        Tienda("Tienda 6", "Descripción simple de la tienda"),
        Tienda("Tienda 7", "Descripción simple de la tienda"),
        Tienda("Tienda 8", "Descripción simple de la tienda"),
        Tienda("Tienda 9", "Descripción simple de la tienda"),
        Tienda("Tienda 10", "Descripción simple de la tienda")
    )

    BaseScreen(
        title = "Tiendas",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Scaffold(
            modifier = Modifier.padding(innerPadding)
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(tiendas) { tienda ->
                        TiendaItem(tienda = tienda, onNavigate = onNavigationSelected)
                    }
                }
            }
        }
    }
}

@Composable
fun TiendaItem(tienda: Tienda, onNavigate: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNavigate("info_tienda") },
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(tienda.nombre, style = MaterialTheme.typography.titleMedium)
                Text(tienda.descripcion, fontSize = 13.sp, color = Color.DarkGray)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Default.ShoppingBag,
                    contentDescription = "Producto",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

data class Tienda(val nombre: String, val descripcion: String)