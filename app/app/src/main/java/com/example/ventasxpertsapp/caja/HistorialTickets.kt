package com.example.ventasxpertsapp.caja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ventasxpertsapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialTicketsV() {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val tickets = listOf(
        TicketInfo("Ticket_202412021982392892192", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892193", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892194", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892195", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892193", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892194", "02/12/2024 11:10:49"),
        TicketInfo("Ticket_202412021982392892195", "02/12/2024 11:10:49")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tickets) { ticket ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(ticket.nombre, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)
                        Text("Fecha: ${ticket.fecha}", fontSize = 13.sp, color = Color.Gray)

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.BlueStrong)
                                ),
                                onClick = { /* Ver ticket */ }) {
                                Icon(Icons.Default.Visibility, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ver")
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.GreenStrong)
                                ),
                                onClick = {
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Descarga completa")
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Descargar")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TicketInfo(val nombre: String, val fecha: String)

