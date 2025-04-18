package com.example.ventasxpertsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.example.ventasxpertsapp.ui.theme.VentasXpertsAppTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Import de las vista base que incluye la barra lateral dinamica
import com.example.ventasxpertsapp.templates.BaseScreen

// Imports de caja
import com.example.ventasxpertsapp.caja.VistaVentas
import com.example.ventasxpertsapp.caja.VistaTicket
import com.example.ventasxpertsapp.caja.HistorialTicketsV

// Imports de Catalogo
import com.example.ventasxpertsapp.catalogo.TiendasCatalogoV
import com.example.ventasxpertsapp.catalogo.TiendaProductosV

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VentasXpertsAppTheme {
                AppMain()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppMain() {
    // Variables para controlar las vistas
    var currentView by remember { mutableStateOf("Caja") }

    // Variables para controlar las ventanas internas entre modulos
    var showHistorial by remember { mutableStateOf(false) }
    var showTicket by remember { mutableStateOf(false) }
    var showTienda by remember { mutableStateOf(false) }


    val screenContent: @Composable (PaddingValues) -> Unit = { padding ->
        when {
            showHistorial -> HistorialTicketsV()
            showTicket -> VistaTicket()
            showTienda -> TiendaProductosV()
            else -> when (currentView) {
                // Agregar los demas cuando se tengan las vistas hechas
                "Catalogo" -> TiendasCatalogoV(onNavigate = { signal ->
                    if (signal == "Tienda") {
                        showTienda = true
                    }
                })
                "Caja" -> VistaVentas(onNavigate = { signal ->
                    if (signal == "Ticket") {
                        showTicket = true
                    }
                })
                else -> VistaVentas()
            }
        }
    }

    // Se llama a la funcion de la vista base que contiene la barra lateral y el contenido
    BaseScreen(
        title = currentView,
        content = { paddingValues -> screenContent(paddingValues) },
        onNavigationSelected = { selected ->
            when (selected) {
                "Historial" -> {
                    showHistorial = true
                    showTicket = false
                    showTienda = false
                }
                "Tienda" -> {
                    showTienda = true
                    showTicket = false
                    showHistorial = false
                }
                else -> {
                    currentView = selected
                    showHistorial = false
                    showTicket = false
                    showTienda = false
                }
            }
        }
    )
}


// Vista previa de las vistas
@Preview(showBackground = true)
@Composable
fun VistaTicketPreview() {
    VentasXpertsAppTheme {
        VistaTicket()
    }
}

@Preview(showBackground = true)
@Composable
fun HistorialTicketsPreview() {
    VentasXpertsAppTheme {
        HistorialTicketsV()
    }
}


@Preview(showBackground = true)
@Composable
fun TiendaProductosPreview() {
    VentasXpertsAppTheme {
        TiendaProductosV()
    }
}

