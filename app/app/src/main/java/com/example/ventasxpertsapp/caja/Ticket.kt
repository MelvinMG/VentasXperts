package com.example.ventasxpertsapp.caja

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
import com.example.ventasxpertsapp.R
import kotlinx.coroutines.launch

@Composable
fun VistaTicket(modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
                    onClick = { }) {
                    Text("Aceptar", color = Color.White)
                }
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.BlueStrong)
                    ),
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Imprimiendo ticket..", duration = SnackbarDuration.Short, withDismissAction = true)
                        }
                    }) {
                    Icon(Icons.Filled.Print, contentDescription = "Imprimir")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Imprimir ticket", color = Color.White)
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                    Text("VentasXpert", fontWeight = FontWeight.Bold, fontSize = 20.sp, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    Text("Fecha: 14/04/2025 14:30:55")
                    Text("Ticket #: 1")
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Producto", fontWeight = FontWeight.Bold)
                        Text("Cantidad")
                        Text("Precio")
                        Text("Subtotal")
                    }

                    Divider(Modifier.padding(vertical = 4.dp))

                    TicketItem("Coca cola 3lts", 2, 50.0)
                    TicketItem("Huevo - 1kg", 2, 30.0)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Total de productos: 4")
                    Text("Precio total: \$160.00")
                    Text("IVA: N/A")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Total: 160.00", fontWeight = FontWeight.Bold)
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

