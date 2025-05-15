package com.app.ventasxpertsmobile.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.ventasxpertsmobile.ui.theme.AzulPrincipal
import androidx.compose.ui.platform.LocalContext

import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.data.auth.UserSessionManager



data class DrawerItem(
    val label: String,
    val icon: @Composable () -> Unit,
    val route: String
)

val drawerItems = listOf(
    DrawerItem("Usuarios",{ Icon(Icons.Filled.Person, contentDescription = "Usuarios") },"usuarios"),

    DrawerItem("Bitácora", { Icon(Icons.Filled.Book, contentDescription = "Bitácora") }, "bitacora"),
    // para mas
)

@Composable
fun NavDrawer(
    selectedRoute: String,
    onItemSelected: (String) -> Unit,
    onLogout: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    Surface(
        shape = RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 6.dp,
        modifier = Modifier
            .fillMaxHeight()
            .width(310.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 24.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = null,
                    tint = AzulPrincipal,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "VentasXpert",
                    fontWeight = FontWeight.Bold,
                    color = AzulPrincipal,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.Close, contentDescription = "Cerrar menú", tint = Color.Gray)
                }
            }
            HorizontalDivider( thickness = 1.dp, color = Color(0xFFE0E0E0))
            Spacer(modifier = Modifier.height(8.dp))

            // Menú
            drawerItems.forEach { item ->
                val selected = selectedRoute == item.route
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .height(48.dp)
                        .background(
                            if (selected) AzulPrincipal.copy(alpha = 0.12f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { onItemSelected(item.route) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .padding(start = 4.dp)
                    ) {
                        item.icon()
                    }
                    Text(
                        text = item.label,
                        color = if (selected) AzulPrincipal else Color.Black,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
                thickness = 1.dp, color = Color(0xFFE0E0E0)
            )

            // Cerrar sesión
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(48.dp)
                    .clickable {
                        RetrofitClient.logout(context) // borra el token local
                        onLogout()
                    }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión",
                    tint = Color(0xFF212121),
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Cerrar sesión",
                    color = Color(0xFF212121),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
