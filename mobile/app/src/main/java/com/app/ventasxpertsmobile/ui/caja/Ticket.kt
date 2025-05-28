package com.app.ventasxpertsmobile.ui.caja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.app.ventasxpertsmobile.ui.caja.CajaViewModel

@Composable
fun TicketScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    ventasViewModel: CajaViewModel,
) {
    val productos by ventasViewModel.productos.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Fecha y hora actual formateada
    val fechaHora = remember {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        now.format(formatter)
    }

    LaunchedEffect(Unit) {
        ventasViewModel.cargarProductos()
    }

    // Calcula total de productos y precio total
    val totalCantidad = productos.sumOf { it.cantidad }
    val totalPrecio = productos.sumOf { it.cantidad * it.producto.precio_tienda }

    BaseScreen(
        title = "Ticket generado",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                        onClick = {
                            ventasViewModel.finalizarVenta(
                                onSuccess = { onNavigationSelected("caja") },
                                onError = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Error al finalizar venta: $message")
                                    }
                                }
                            )
                        }
                    ) {
                        Text("Finalizar venta", color = Color.White)
                    }

                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(id = R.color.BlueStrong)
                        ),
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Imprimiendo ticket..",
                                    duration = SnackbarDuration.Short,
                                    withDismissAction = true
                                )
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Print, contentDescription = "Imprimir")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Imprimir", color = Color.White)
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        ) { scaffoldPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .padding(horizontal = 24.dp)
                    .padding(top = 60.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text("Venta exitosa", style = MaterialTheme.typography.headlineSmall)

                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "VentasXpert",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text("Fecha: $fechaHora")
                        Text("Ticket #: 1") // Si quieres, puedes hacerlo dinámico
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Producto", fontWeight = FontWeight.Bold)
                            Text("Cantidad", fontWeight = FontWeight.Bold)
                            Text("Precio", fontWeight = FontWeight.Bold)
                            Text("Subtotal", fontWeight = FontWeight.Bold)
                        }

                        HorizontalDivider(Modifier.padding(vertical = 4.dp))

                        productos.forEach { producto ->
                            TicketItem(
                                nombre = producto.producto.nombre,
                                cantidad = producto.cantidad,
                                precio = producto.producto.precio_tienda
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Total de productos: $totalCantidad")
                        Text("Precio total: \$${"%.2f".format(totalPrecio)}")
                        Text("IVA: N/A")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Total: ${"%.2f".format(totalPrecio)}", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(nombre: String, cantidad: Int, precio: Double) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(nombre)
        Text(cantidad.toString())
        Text("$${"%.2f".format(precio)}")
        Text("$${"%.2f".format(cantidad * precio)}")
    }
}
