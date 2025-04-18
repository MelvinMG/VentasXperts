package com.example.ventasxpertsapp.caja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Close
import androidx.compose.foundation.clickable
import com.example.ventasxpertsapp.R
import androidx.compose.ui.res.colorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaVentas(modifier: Modifier = Modifier, onNavigate: (String) -> Unit = {}) {
    val scope = rememberCoroutineScope()
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }


    val productos = remember {
        mutableStateListOf(
            Producto("Coca - Cola 3 lts", 2, 50.0),
            Producto("Huevo - 1kg", 2, 30.0),
            Producto("Leche - 2L", 1, 25.0),
            Producto("Pan Bimbo", 3, 20.0),
        )
    }

    val productosFiltrados = productos.filter {
        it.nombre.contains(query, ignoreCase = true)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* acción cámara */ }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Abrir cámara")
            }
        },
        bottomBar = {
            FooterVenta(productos, onNavigate)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 30.dp)
        ) {
            SearchBar(
                query = query,
                onQueryChange = { query = it },
                onSearch = {},
                active = active,
                onActiveChange = { active = it },
                placeholder = { Text("Buscar productos") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (active) {
                        IconButton(onClick = {
                            query = ""
                            active = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar búsqueda"
                            )
                        }
                    }
                }
            ) {
                Text(
                    "Sugerencia: Coca",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            query = "Coca"
                            active = false
                        }
                )
                Text(
                    "Sugerencia: Huevo",
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            query = "Huevo"
                            active = false
                        }
                )
            }

            Text(
                text = "Total de productos: ${productosFiltrados.sumOf { it.cantidad }}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    items(productosFiltrados) { producto ->
                        ProductoItem(producto)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(producto: Producto) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Cantidad: ${producto.cantidad}")
            Text("Precio unitario: MNX ${producto.precio}")
            Text("Subtotal: MNX ${producto.cantidad * producto.precio}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.BlueStrong)
                    ),
                    onClick = { producto.cantidad++ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.BlueStrong)
                    ),
                    onClick = { if (producto.cantidad > 0) producto.cantidad-- }) {
                    Icon(Icons.Filled.Remove, contentDescription = "Quitar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Quitar")
                }
                IconButton(onClick = { /* eliminar producto */ }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
fun FooterVenta(productos: MutableList<Producto>, onNavigate: (String) -> Unit) {
    val total = productos.sumOf { it.precio * it.cantidad }
    var showDialog_cancelar by remember { mutableStateOf(false) }
    var showDialog_finalizar by remember { mutableStateOf(false) }

    if (showDialog_cancelar) {
        AlertDialog(
            onDismissRequest = { showDialog_cancelar = false },
            title = { Text("¿Cancelar venta?") },
            text = { Text("Se eliminarán todos los productos del carrito actual.") },
            confirmButton = {
                TextButton(onClick = {
                    productos.clear()
                    showDialog_cancelar = false
                }) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog_cancelar = false }) {
                    Text("No, seguir venta")
                }
            }
        )
    }

    if (showDialog_finalizar) {
        AlertDialog(
            onDismissRequest = { showDialog_finalizar = false },
            title = { Text("¿Confirmar cobro?") },
            text = { Text("Se realizara el cobro de la venta actual.") },
            confirmButton = {
                TextButton(onClick = {
                    productos.clear()
                    showDialog_finalizar = false
                    onNavigate("Ticket")
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog_finalizar = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Costo total de los productos: MNX ${"%.2f".format(total)}", fontSize = 16.sp)
        Text("IVA calculado: N/A")
        Text("Descuento: N/A")
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.RedStrong)
                ),
                onClick = { showDialog_cancelar = true }
            ) {
                Text("Cancelar venta", color = Color.White)
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.BlueStrong)
                ),
                onClick = { showDialog_finalizar = true }
            ) {
                Text("Finalizar venta", color = Color.White)
            }
        }
    }
}

data class Producto(
    val nombre: String,
    var cantidad: Int,
    val precio: Double
)