package com.app.ventasxpertsmobile.ui.templates


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.ventasxpertsmobile.R
import com.app.ventasxpertsmobile.data.network.RetrofitClient
import com.app.ventasxpertsmobile.ui.navigation.NavigationItem
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Boxes
import compose.icons.fontawesomeicons.solid.StoreAlt
import compose.icons.fontawesomeicons.solid.Truck
import compose.icons.fontawesomeicons.solid.UserLock
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import com.app.ventasxpertsmobile.data.auth.UserSessionManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarVentasXpert(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String = "",
    onMenuClick: (Any) -> Unit = {}
) {
    val expanded = remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_1_sin),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .height(28.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    if (drawerState.isClosed) drawerState.open()
                    else drawerState.close()
                }
            }) {
                Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
            }
        },
        actions = {
            Box {
                IconButton(onClick = { expanded.value = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Más opciones")
                }
                DropdownMenu(
                    expanded = expanded.value,
                    onDismissRequest = { expanded.value = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Historial de ventas") },
                        onClick = {
                            expanded.value = false
                            onMenuClick("Historial de ventas")
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BaseScreen(
    title: String,
    onLogout: () -> Unit,
    onNavigationSelected: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(title) }
    val context = LocalContext.current
    val roles = UserSessionManager.getRoles(context) // Aqui vamos a obtener el rol del usuario ingresado
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Logo y botón cerrar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_1_sin),
                                contentDescription = "Logo",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("VentasXpert", style = MaterialTheme.typography.titleMedium)
                        }
                        IconButton(onClick = {
                            scope.launch { drawerState.close() }
                        }) {
                            Icon(Icons.Filled.Close, contentDescription = "Cerrar Drawer")
                        }
                    }

                    HorizontalDivider()

                    // Aquí el menú filtrado por roles
                    if (roles.contains("Administrador") || roles.contains("Gerente") || roles.contains("Cajero")) {
                        NavigationDrawerItem(
                            label = { Text("Catalogo") },
                            selected = selectedItem.value == "Catalogo",
                            onClick = {
                                selectedItem.value = NavigationItem.Catalogo.label
                                onNavigationSelected(NavigationItem.Catalogo.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.StoreAlt,
                                    contentDescription = "Catalogo",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    if (roles.contains("Administrador") || roles.contains("Gerente") || roles.contains("Cajero")) {
                        NavigationDrawerItem(
                            label = { Text("Caja") },
                            selected = selectedItem.value == "Caja",
                            onClick = {
                                selectedItem.value = NavigationItem.Caja.label
                                onNavigationSelected(NavigationItem.Caja.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Outlined.ShoppingCart,
                                    contentDescription = "Caja",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    if (roles.contains("Administrador") || roles.contains("Gerente") ){
                        NavigationDrawerItem(
                            label = { Text("Inventario") },
                            selected = selectedItem.value == "Inventario",
                            onClick = {
                                selectedItem.value = "Inventario"
                                onNavigationSelected(NavigationItem.Inventario.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.Boxes,
                                    contentDescription = "Inventario",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    if (roles.contains("Administrador") || roles.contains("Gerente")) {
                        NavigationDrawerItem(
                            label = { Text("Proveedores") },
                            selected = selectedItem.value == "Proveedores",
                            onClick = {
                                selectedItem.value = NavigationItem.Proveedor.label
                                onNavigationSelected(NavigationItem.Proveedor.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.Truck,
                                    contentDescription = "Proveedores",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    if (roles.contains("Administrador")) {
                        NavigationDrawerItem(
                            label = { Text("Usuarios") },
                            selected = selectedItem.value == NavigationItem.Usuarios.label,
                            onClick = {
                                selectedItem.value = NavigationItem.Usuarios.label
                                onNavigationSelected(NavigationItem.Usuarios.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = FontAwesomeIcons.Solid.UserLock,
                                    contentDescription = "Usuarios",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                    if (roles.contains("Administrador") || roles.contains("Gerente") ) {
                        NavigationDrawerItem(
                            label = { Text("Bitácora") },
                            selected = selectedItem.value == NavigationItem.Bitacora.label,
                            onClick = {
                                selectedItem.value = NavigationItem.Bitacora.label
                                onNavigationSelected(NavigationItem.Bitacora.route)
                                scope.launch { drawerState.close() }
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Book,
                                    contentDescription = "Bitácora",
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text("Configuración") },
                        selected = selectedItem.value == "Configuración",
                        onClick = {
                            selectedItem.value = "Configuración"
                            onNavigationSelected("Configuración")
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings, // Usa el icono de Material o el de FontAwesome si tienes uno
                                contentDescription = "Configuración",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )


                    // Agrega un simple texto
                    val userFullName = UserSessionManager.getUserFullName(context)
                    Text(
                        text = userFullName,
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        onClick = {
                            RetrofitClient.logout(context)
                            onLogout()

                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopBarVentasXpert(
                    scope = scope,
                    drawerState = drawerState,
                    title = title,
                    onMenuClick = { selectedOption ->
                        if (selectedOption == "Historial de ventas") {
                            onNavigationSelected("Historial")
                        }
                    }
                )
            }
        ) {  innerPadding ->
            content(innerPadding) // Contenido cambiable, dependera de lo que se envies en la funcion
        }
    }
}