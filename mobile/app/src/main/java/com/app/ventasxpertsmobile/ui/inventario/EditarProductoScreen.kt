package com.app.ventasxpertsmobile.ui.inventario
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.ventasxpertsmobile.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarProductoScreen(
    nombreProducto: String = "Nuevo producto",
    onCancel: () -> Unit = {},
    onCreate: () -> Unit = {},
    onLogout: () -> Unit = {},
    navController: NavController // Pasamos el NavController para manejar la navegación
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra superior con la flecha de regreso, ícono de carrito vacío y título
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flecha de regreso
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Ícono del carrito vacío
            Image(
                painter = painterResource(id = R.drawable.logo_1_sin),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(28.dp)
                    .padding(end = 8.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Título de la pantalla
            Text(
                text = nombreProducto,
                style = MaterialTheme.typography.titleLarge


            )




        }

        // Imagen grande con nombre del producto encima
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(Color(0xFFD3D3D3))
        ) {
            Icon(
                Icons.Default.Image,
                contentDescription = null,
                tint = Color(0xFFAAAAAA),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(90.dp)
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

        // Formulario
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Descripción del producto",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            var codigo by remember { mutableStateOf("") }
            var nombre by remember { mutableStateOf("") }
            var categoria by remember { mutableStateOf("Categoría") }
            val categorias = listOf("Categoría", "Refresco", "Lácteos", "Cereales", "Botanas", "Otros")
            var expandedCategoria by remember { mutableStateOf(false) }
            var precioProveedor by remember { mutableStateOf("") }
            var precioCliente by remember { mutableStateOf("") }
            var gananciaPorcentaje by remember { mutableStateOf("") }
            var gananciaDinero by remember { mutableStateOf("") }

            // Inputs de texto para editar el producto
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    label = { Text("Categoría") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expandedCategoria = true },
                    enabled = false,
                    trailingIcon = {
                        IconButton(onClick = { expandedCategoria = true }) {
                            Icon(Icons.Filled.ArrowDropDown, contentDescription = "Seleccionar categoría")
                        }
                    }
                )
                DropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    categorias.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat) },
                            onClick = {
                                categoria = cat
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

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

            OutlinedTextField(
                value = gananciaPorcentaje,
                onValueChange = { gananciaPorcentaje = it },
                label = { Text("Ganancia %") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true
            )

            OutlinedTextField(
                value = gananciaDinero,
                onValueChange = { gananciaDinero = it },
                label = { Text("Ganancia \$") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
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
                        .padding(end = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = onCreate,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF223A7A),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text("Guardar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditarProductoScreenPreview() {
    val navController = rememberNavController() // Agregamos el NavController
    EditarProductoScreen(
        nombreProducto = "Producto de prueba",
        onCancel = { navController.popBackStack() },  // Regresa al backstack al presionar "Cancelar"
        onCreate = { navController.popBackStack() },  // Regresa al backstack al presionar "Guardar"
        navController = navController // Pasamos el navController
    )
}
