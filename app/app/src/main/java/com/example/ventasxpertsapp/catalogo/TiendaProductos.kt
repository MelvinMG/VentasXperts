package com.example.ventasxpertsapp.catalogo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TiendaProductosV() {
    val productos = listOf(
        ProductoTienda("Producto 1", 100.00),
        ProductoTienda("Producto 2", 100.00),
        ProductoTienda("Producto 3", 100.00),
        ProductoTienda("Producto 4", 100.00),
        ProductoTienda("Producto 5", 100.00),
        ProductoTienda("Producto 2", 100.00),
        ProductoTienda("Producto 3", 100.00),
        ProductoTienda("Producto 4", 100.00),
        ProductoTienda("Producto 1", 100.00),
        ProductoTienda("Producto 2", 100.00),
        ProductoTienda("Producto 3", 100.00),
        ProductoTienda("Producto 4", 100.00)
    )

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            // Información de la tienda
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Tienda 1 ejemplo",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp).padding(top = 60.dp)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor. jksjk jsakjskajs aksjaksjaksjak aksamksjak aksjaksjak.",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text("Productos disponibles", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(productos) { producto ->
                    ProductoCard(producto)
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: ProductoTienda) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                Text(producto.nombre, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                Text("Precio: MNX ${"%.2f".format(producto.precio)}", fontSize = 14.sp, color = Color.Gray)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Default.Inventory,
                    contentDescription = "Producto",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )

            }
        }
    }
}

data class ProductoTienda(val nombre: String, val precio: Double)