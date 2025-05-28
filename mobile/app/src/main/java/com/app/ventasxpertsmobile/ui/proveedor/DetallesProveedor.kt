package com.app.ventasxpertsmobile.ui.proveedor

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.data.model.Proveedor
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun DetalleProveedorScreen(
    proveedorId: Int,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var proveedor by remember { mutableStateOf<Proveedor?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(proveedorId) {
        try {
            val response = withContext(Dispatchers.IO) {
                RetrofitClient.api.getProveedorById(proveedorId).execute()
            }
            if (response.isSuccessful) {
                proveedor = response.body()
            } else {
                errorMsg = "Error ${response.code()}: ${response.message()}"
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            errorMsg = e.localizedMessage ?: "Error desconocido"
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        } finally {
            isLoading = false
        }
    }

    BaseScreen(
        title = "Detalles del proveedor",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> CircularProgressIndicator()
                errorMsg != null -> Text(text = errorMsg ?: "Error inesperado")
                proveedor != null -> ProveedorDetalleContent(proveedor!!)
                else -> Text("Proveedor no encontrado")
            }
        }
    }
}

@Composable
fun ProveedorDetalleContent(proveedor: Proveedor) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(AzulPrincipal),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = proveedor.nombre.take(1).uppercase(),
                style = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onPrimary)
            )
        }
        Spacer(Modifier.height(16.dp))

        Text(
            text = proveedor.nombre,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = "Teléfono: ${proveedor.telefono}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Correo: ${proveedor.correo}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))


    }
}

@Composable
fun ActionRow(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            color = AzulPrincipal
        )
        Icon(
            imageVector = FontAwesomeIcons.Solid.ArrowRight,
            contentDescription = null,
            tint = AzulPrincipal,
            modifier = Modifier.size(28.dp)
        )
    }
}
