package com.example.ventasxpertsapp.templates

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.AutoAwesomeMotion
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.ventasxpertsapp.R
import compose.icons.fontawesomeicons.solid.Boxes
import compose.icons.fontawesomeicons.solid.ShoppingBag
import compose.icons.fontawesomeicons.solid.StoreAlt
import compose.icons.fontawesomeicons.solid.Truck
import compose.icons.fontawesomeicons.solid.TruckLoading
import compose.icons.fontawesomeicons.solid.UserLock
import compose.icons.fontawesomeicons.solid.Users

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
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier,
                textAlign = TextAlign.Start
            )
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
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
    onNavigationSelected: (String) -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val selectedItem = remember { mutableStateOf(title) }

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
                                painter = painterResource(id = R.drawable.icon_page),
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

                    // Ítems del menú
                    NavigationDrawerItem(
                        label = { Text("Catalogo") },
                        selected = selectedItem.value == "Catalogo",
                        onClick = {
                            selectedItem.value = "Catalogo"
                            onNavigationSelected("Catalogo")
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

                    NavigationDrawerItem(
                        label = { Text("Caja") },
                        selected = selectedItem.value == "Caja",
                        onClick = {
                            selectedItem.value = "Caja"
                            onNavigationSelected("Caja")
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.ShoppingCart,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Inventario") },
                        selected = selectedItem.value == "Inventario",
                        onClick = {
                            selectedItem.value = "Inventario"
                            onNavigationSelected("Inventario")
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

                    NavigationDrawerItem(
                        label = { Text("Proveedores") },
                        selected = selectedItem.value == "Proveedores",
                        onClick = {
                            selectedItem.value = "Proveedores"
                            onNavigationSelected("Proveedores")
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

                    NavigationDrawerItem(
                        label = { Text("Permisos y roles") },
                        selected = selectedItem.value == "Permisos y roles",
                        onClick = {
                            selectedItem.value = "Permisos y roles"
                            onNavigationSelected("Permisos y roles")
                            scope.launch { drawerState.close() }
                        },
                        icon = {
                            Icon(
                                imageVector = FontAwesomeIcons.Solid.UserLock,
                                contentDescription = "Users",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Agrega un simple texto
                    Text(
                        text = "Juan Pérez",
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    NavigationDrawerItem(
                        label = { Text("Cerrar sesión") },
                        selected = false,
                        onClick = {
                            // Acción de logout
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = null
                            )
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
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
            content(innerPadding) // Contenido cambiable, dependera de lo que se envie en la funcion
        }
    }
}