package com.app.ventasxpertsmobile.ui.catalogo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.data.model.Tienda
import com.app.ventasxpertsmobile.data.model.Producto

@Composable
fun TiendaProductosScreen(
    tiendaId: Int,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    viewModel: CatalogoViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // Observar la tienda seleccionada desde el ViewModel
    val tienda = viewModel.getTiendaById(tiendaId)

    if (tienda == null) {
        // Mostrar loading o mensaje si no está disponible aún
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Mostrar pantalla con la tienda cargada
    TiendaProductosContent(
        tienda = tienda,
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaProductosContent(
    tienda: Tienda,
    onLogout: () -> Unit,
    onNavigationSelected: (String) -> Unit
) {
    val productos = tienda.productos

    BaseScreen(
        title = tienda.nombre,
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
                // Descripción de la tienda
                Text(
                    text = tienda.descripcion,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Text(
                    "Productos disponibles",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productos) { producto ->
                        ProductoCard(producto)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F2F2))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(producto.nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Precio: MXN ${producto.precio_tienda}", fontSize = 14.sp, color = Color.Gray)
            }
            Icon(
                imageVector = Icons.Default.Inventory,
                contentDescription = "Producto",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}