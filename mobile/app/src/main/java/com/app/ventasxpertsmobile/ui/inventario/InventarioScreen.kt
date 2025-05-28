package com.app.ventasxpertsmobile.ui.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.tooling.preview.Preview

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val estadoStock: String,
    val categoria: String
)

@Composable
fun SearchAndFilterBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onAddClick: () -> Unit,
    stockFilter: String,
    onStockFilterChange: (String) -> Unit,
    categoryFilter: String,
    onCategoryFilterChange: (String) -> Unit,
    stockOptions: List<String>,
    categoryOptions: List<String>
) {
    var stockMenuExpanded by remember { mutableStateOf(false) }
    var categoryMenuExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChanged,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            placeholder = { Text("Buscar productos") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(28.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(48.dp)
                .background(AzulPrincipal, shape = RoundedCornerShape(24.dp))
        ) {
            Icon(Icons.Default.Add, contentDescription = "Agregar", tint = Color.White)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box {
            IconButton(
                onClick = { stockMenuExpanded = true },
                modifier = Modifier
                    .size(48.dp)
                    .background(AzulPrincipal, shape = RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar Stock", tint = Color.White)
            }
            DropdownMenu(
                expanded = stockMenuExpanded,
                onDismissRequest = { stockMenuExpanded = false }
            ) {
                stockOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onStockFilterChange(option)
                            stockMenuExpanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box {
            IconButton(
                onClick = { categoryMenuExpanded = true },
                modifier = Modifier
                    .size(48.dp)
                    .background(AzulPrincipal, shape = RoundedCornerShape(14.dp))
            ) {
                Icon(Icons.Default.Category, contentDescription = "Filtrar Categoría", tint = Color.White)
            }
            DropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = { categoryMenuExpanded = false }
            ) {
                categoryOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onCategoryFilterChange(option)
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoCard(
    producto: Producto,
    onAgregar: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit  // Nuevo callback para eliminar
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, Color(0xFFE3E3E3)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color(0xFFD3D3D3), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFFB0B0B0), modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "Precio: MNX \$${"%.2f".format(producto.precio)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF323232)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onAgregar) {
                    Icon(Icons.Default.AddCircle, contentDescription = "Agregar", tint = AzulPrincipal, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = onEditar) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = AzulPrincipal, modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    productos: List<Producto>,
    onAgregar: () -> Unit,
    onEditar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit,  // Nuevo callback para eliminar
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }
    var stockFilter by remember { mutableStateOf("Todos") }
    var categoryFilter by remember { mutableStateOf("Todas") }

    val stockOptions = listOf("Todos", "Sin stock", "Mínimo de stock", "Suficiente stock")
    val categoryOptions = listOf("Todas") + productos.map { it.categoria }.distinct()

    // Estado para manejar modal de agregar cantidad
    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var cantidadStock by remember { mutableStateOf("") }

    // Estado para manejar modal de confirmación eliminación
    var productoEliminar by remember { mutableStateOf<Producto?>(null) }

    val productosFiltrados = productos.filter { producto ->
        (stockFilter == "Todos" || producto.estadoStock == stockFilter) &&
                (categoryFilter == "Todas" || producto.categoria == categoryFilter) &&
                producto.nombre.contains(searchText, ignoreCase = true)
    }

    BaseScreen(
        title = "Inventario",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Productos disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )
            SearchAndFilterBar(
                searchText = searchText,
                onSearchTextChanged = { searchText = it },
                onAddClick = onAgregar,
                stockFilter = stockFilter,
                onStockFilterChange = { stockFilter = it },
                categoryFilter = categoryFilter,
                onCategoryFilterChange = { categoryFilter = it },
                stockOptions = stockOptions,
                categoryOptions = categoryOptions
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(productosFiltrados) { producto ->
                    ProductoCard(
                        producto = producto,
                        onAgregar = {
                            productoSeleccionado = producto
                            cantidadStock = ""
                        },
                        onEditar = { onEditar(producto) },
                        onEliminar = { productoEliminar = producto } // Abrir modal eliminar
                    )
                }
            }
        }

        // Modal agregar cantidad (igual que antes)
        if (productoSeleccionado != null) {
            AlertDialog(
                onDismissRequest = { productoSeleccionado = null },
                title = { Text("Agregar a stock") },
                text = {
                    Column {
                        Text("Ingrese cantidad a agregar para: ${productoSeleccionado?.nombre}")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = cantidadStock,
                            onValueChange = { newVal ->
                                if (newVal.all { it.isDigit() }) {
                                    cantidadStock = newVal
                                }
                            },
                            label = { Text("Cantidad") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            // Aquí iría la lógica para actualizar stock
                            productoSeleccionado = null
                        }
                    ) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { productoSeleccionado = null }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Modal confirmación eliminar producto
        if (productoEliminar != null) {
            AlertDialog(
                onDismissRequest = { productoEliminar = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Está seguro que desea eliminar el producto ${productoEliminar?.nombre}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEliminar(productoEliminar!!)
                            productoEliminar = null
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { productoEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InventarioScreenPreview() {
    val productos = listOf(
        Producto(1, "Coca-Cola", 18.00, "Suficiente stock", "Refrescos"),
        Producto(2, "Fanta", 17.00, "Mínimo de stock", "Refrescos"),
        Producto(3, "Corn Flakes", 42.00, "Sin stock", "Cereal"),
        Producto(4, "Yogur", 25.00, "Suficiente stock", "Lácteos"),
        Producto(5, "Cheetos", 14.00, "Suficiente stock", "Botanas")
    )
    InventarioScreen(
        productos = productos,
        onAgregar = {},
        onEliminar = {},
        onEditar = {}
    )
}
