package com.app.ventasxpertsmobile.ui.inventario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.data.model.CategoriaDTO
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(
    nombreProducto: String = "Nuevo producto",
    viewModel: ProductoViewModel = viewModel(),
    onCancel: () -> Unit = {},
    navController: NavController
) {
    var codigo by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf<CategoriaDTO?>(null) }
    var expandedCategoria by remember { mutableStateOf(false) }
    var precioProveedor by remember { mutableStateOf("") }
    var precioCliente by remember { mutableStateOf("") }
    var stockInventario by remember { mutableStateOf("") }
    var stockMinimo by remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    val isLoading = viewModel.isLoading.value

    LaunchedEffect(Unit) {
        viewModel.cargarCategorias()
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.White),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Regresar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.logo_1_sin),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(28.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = nombreProducto,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE53935),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = { mostrarConfirmacion = true },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrincipal,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Text("Guardar")
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Recuadro con ícono simulando imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(Color(0xFFD3D3D3)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        tint = Color(0xFFAAAAAA),
                        modifier = Modifier.size(90.dp)
                    )
                    Text(
                        text = nombreProducto,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = codigo,
                    onValueChange = { codigo = it },
                    label = { Text("Código") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true,
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCategoria,
                    onExpandedChange = { expandedCategoria = !expandedCategoria }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = categoriaSeleccionada?.nombre ?: "Seleccionar categoría",
                        onValueChange = {},
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedCategoria,
                        onDismissRequest = { expandedCategoria = false }
                    ) {
                        viewModel.categorias.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.nombre) },
                                onClick = {
                                    categoriaSeleccionada = cat
                                    expandedCategoria = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = stockInventario,
                    onValueChange = { stockInventario = it },
                    label = { Text("Stock Inventario") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = stockMinimo,
                    onValueChange = { stockMinimo = it },
                    label = { Text("Stock Mínimo") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(
                    value = precioProveedor,
                    onValueChange = { precioProveedor = it },
                    label = { Text("Precio proveedor") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = precioCliente,
                    onValueChange = { precioCliente = it },
                    label = { Text("Precio cliente") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    singleLine = true
                )

                errorMessage.value?.let { msg ->
                    Text(
                        text = msg,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(80.dp)) // Para que no tape el último campo con botones
            }
        }
    )

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = { Text("Confirmar guardado") },
            text = { Text("¿Estás seguro que deseas guardar el producto \"$nombre\"?") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarConfirmacion = false
                    guardarProducto(
                        codigo, nombre, categoriaSeleccionada, stockInventario, stockMinimo,
                        precioProveedor, precioCliente, viewModel, errorMessage,
                        onSuccess = {
                            errorMessage.value = null
                            navController.popBackStack()
                        },
                        onError = { error -> errorMessage.value = error }
                    )
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacion = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

private fun guardarProducto(
    codigo: String,
    nombre: String,
    categoriaSeleccionada: CategoriaDTO?,
    stockInventario: String,
    stockMinimo: String,
    precioProveedor: String,
    precioCliente: String,
    viewModel: ProductoViewModel,
    errorMessage: MutableState<String?>,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    val stockInv = stockInventario.toIntOrNull() ?: 0
    val stockMin = stockMinimo.toIntOrNull() ?: 0
    val precioProv = precioProveedor.toDoubleOrNull() ?: 0.0
    val precioCli = precioCliente.toDoubleOrNull() ?: 0.0
    val categoriaId = categoriaSeleccionada?.id ?: -1

    if (categoriaId == -1) {
        errorMessage.value = "Debe seleccionar una categoría"
        return
    }

    viewModel.agregarProducto(
        codigo = codigo,
        nombre = nombre,
        categoriaId = categoriaId,
        stockInventario = stockInv,
        stockMinimo = stockMin,
        precioProveedor = precioProv,
        precioCliente = precioCli,
        gananciaPorcentaje = null,
        gananciaDinero = null,
        onSuccess = onSuccess,
        onError = onError
    )
}
