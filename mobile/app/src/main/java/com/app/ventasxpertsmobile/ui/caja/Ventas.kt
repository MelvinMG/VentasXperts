package com.app.ventasxpertsmobile.ui.caja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.res.colorResource
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.caja.CajaViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VentasScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    ventasViewModel: CajaViewModel,
) {
    val productos by ventasViewModel.productos.collectAsState()

    val productosCatalogo by ventasViewModel.productosCatalogo.collectAsState()

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val productosFiltrados = productos.filter {
        it.producto.nombre.contains(query, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        ventasViewModel.cargarProductos()
        ventasViewModel.cargarProductosCatalogo()
    }

    BaseScreen(
        title = "Caja",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Scaffold(
            bottomBar = {
                FooterVenta(
                    ventasViewModel = ventasViewModel,
                    onNavigationSelected = onNavigationSelected
                )
            },
            modifier = Modifier.padding(innerPadding) // Para respetar el padding del Drawer
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
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
                                Icon(Icons.Default.Close, contentDescription = "Cerrar búsqueda")
                            }
                        }
                    }
                ) {
                    productosCatalogo.forEach { producto ->
                        Text(
                            "Sugerencia: ${producto.nombre}",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    // Agrega el producto al carrito
                                    ventasViewModel.agregarUnidad(producto.id)
                                    query = producto.nombre
                                    active = false
                                }
                        )
                    }
                }

                Text(
                    text = "Total de productos: ${productosFiltrados.sumOf { it.cantidad }}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                ) {
                    items(productosFiltrados) { carritoProducto ->
                        ProductoItem(
                            id = carritoProducto.producto.id,
                            nombre = carritoProducto.producto.nombre,
                            cantidad = carritoProducto.cantidad,
                            precio = carritoProducto.producto.precio_tienda,
                            onAgregar = { id -> ventasViewModel.agregarUnidad(id) },
                            onQuitarUnidad = { id -> ventasViewModel.restarUnidad(id) },
                            onEliminar = { id -> ventasViewModel.quitarProducto(id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItem(
    id: Int,
    nombre: String,
    cantidad: Int,
    precio: Double,
    onAgregar: (Int) -> Unit,
    onQuitarUnidad: (Int) -> Unit,
    onEliminar: (Int) -> Unit
) {
    Card(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = nombre, style = MaterialTheme.typography.titleMedium)
            Text("Cantidad: $cantidad")
            Text("Precio unitario: MNX $precio")
            Text("Subtotal: MNX ${cantidad * precio}")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.BlueStrong)),
                    onClick = { onAgregar(id) }
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Agregar")
                }
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.BlueStrong)),
                    onClick = { onQuitarUnidad(id) }
                ) {
                    Icon(Icons.Filled.Remove, contentDescription = "Quitar")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Quitar")
                }
                IconButton(onClick = { onEliminar(id) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}


@Composable
fun FooterVenta(
    ventasViewModel: CajaViewModel,
    onNavigationSelected: (String) -> Unit
) {
    val productos by ventasViewModel.productos.collectAsState()

    var showDialog_cancelar by remember { mutableStateOf(false) }
    var showDialog_finalizar by remember { mutableStateOf(false) }

    val totalCost = productos.sumOf { it.cantidad * it.producto.precio_tienda }

    if (showDialog_cancelar) {
        AlertDialog(
            onDismissRequest = { showDialog_cancelar = false },
            title = { Text("¿Cancelar venta?") },
            text = { Text("Se eliminarán todos los productos del carrito actual.") },
            confirmButton = {
                TextButton(onClick = {
                    ventasViewModel.vaciarCarrito() // <-- llamada a vaciar carrito
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
                    //productos.clear()
                    showDialog_finalizar = false
                    onNavigationSelected("ticket")
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
        Text(
            "Costo total de los productos: MNX ${"%.2f".format(totalCost)}",
            fontSize = 16.sp
        )
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