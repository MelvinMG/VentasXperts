package com.app.ventasxpertsmobile.ui.caja

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.app.ventasxpertsmobile.R
import kotlinx.coroutines.launch
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect

import android.content.Intent
import android.content.ActivityNotFoundException
import android.net.Uri
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialTicketsScreen(
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {},
    viewModel: CajaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val tickets by viewModel.tickets.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.cargarHistorialTickets()
    }

    BaseScreen(
        title = "Historial de Ventas",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.padding(innerPadding)
        ) { scaffoldPadding ->

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = error ?: "Error", color = Color.Red)
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(tickets) { ticket ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = ticket.name,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Fecha: ${ticket.creation_time}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.End
                                    ) {
                                        Button(
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = colorResource(id = R.color.BlueStrong)
                                            ),
                                            onClick = {
                                                val fullUrl = "http://10.31.10.225:8000${ticket.path}"

                                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                                    setDataAndType(Uri.parse(fullUrl), "application/pdf")
                                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                                }

                                                try {
                                                    context.startActivity(intent)
                                                } catch (e: ActivityNotFoundException) {
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar("No se encontró una app para abrir el PDF.")
                                                    }
                                                }
                                            }
                                        ) {
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
        }
    }
}


