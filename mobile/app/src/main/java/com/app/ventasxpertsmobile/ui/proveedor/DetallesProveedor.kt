package com.app.ventasxpertsmobile.ui.proveedor

import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.ui.templates.BaseScreen
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import com.app.ventasxpertsmobile.ui.theme.Blanco1
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.solid.ArrowRight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import compose.icons.fontawesomeicons.Solid

@Composable
fun DetalleProveedorScreen(
    proveedorId: Int?,
    onLogout: () -> Unit = {},
    onNavigationSelected: (String) -> Unit = {}
) {
    val proveedor = proveedoresDemo.find { it.nombre.hashCode() == proveedorId }

    BaseScreen(
        title = "Detalles del proveedor",
        onLogout = onLogout,
        onNavigationSelected = onNavigationSelected
    ) { innerPadding ->

        proveedor?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = it.logoRes),
                    contentDescription = "Logo del proveedor",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = it.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Descripción: ${it.descripcion}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Teléfono: ${it.telefono}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Correo: ${it.correo}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onNavigationSelected("editar_proveedor/${it.nombre}") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrincipal)
                ) {
                    Icon(
                        FontAwesomeIcons.Solid.ArrowRight,
                        contentDescription = "Editar proveedor",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Editar proveedor",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Blanco1
                    )
                }
            }
        }
    }
}
