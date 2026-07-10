package com.inventario.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.inventario.app.ui.auth.LoginScreen
import com.inventario.app.ui.cliente.ClienteFormScreen
import com.inventario.app.ui.cliente.ClientesScreen
import com.inventario.app.ui.categoria.CategoriasScreen
import com.inventario.app.ui.home.HomeScreen
import com.inventario.app.ui.movimiento.MovimientoFormScreen
import com.inventario.app.ui.movimiento.MovimientosScreen
import com.inventario.app.ui.onboarding.OrganizationSelectorScreen
import com.inventario.app.ui.onboarding.WorkspaceSelectorScreen
import com.inventario.app.ui.producto.ProductoFormScreen
import com.inventario.app.ui.producto.ProductosScreen
import com.inventario.app.ui.settings.SettingsScreen
import com.inventario.app.ui.venta.VentaFormScreen
import com.inventario.app.ui.venta.VentasScreen
import com.inventario.app.ui.components.NuevoBottomSheet

private val mainRoutes = setOf(
    Screen.Home.route,
    Screen.Productos.route,
    Screen.Movimientos.route,
    Screen.Ventas.route,
    Screen.Clientes.route,
    Screen.Settings.route,
    Screen.Categorias.route
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in mainRoutes
    var showFabSheet by remember { mutableStateOf(false) }

    if (showBottomBar) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            navController.navigate(Screen.Home.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Inventory2, contentDescription = "Productos") },
                        label = { Text("Productos") },
                        selected = currentRoute == Screen.Productos.route,
                        onClick = {
                            navController.navigate(Screen.Productos.route) {
                                launchSingleTop = true
                            }
                        }
                    )

                    Box(modifier = Modifier.weight(1f))

                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.SwapHoriz, contentDescription = "Movimientos") },
                        label = { Text("Movimientos") },
                        selected = currentRoute == Screen.Movimientos.route,
                        onClick = {
                            navController.navigate(Screen.Movimientos.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Settings, contentDescription = "Configuración") },
                        label = { Text("Config") },
                        selected = currentRoute == Screen.Settings.route,
                        onClick = {
                            navController.navigate(Screen.Settings.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showFabSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Nuevo",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center
        ) { padding ->
            Box(Modifier.padding(padding)) {
                AppNavHost(
                    navController = navController,
                    startDestination = startDestination,
                    onFabSheetDismiss = { showFabSheet = false },
                    showFabSheet = showFabSheet
                )
            }
        }
    } else {
        AppNavHost(
            navController = navController,
            startDestination = startDestination,
            onFabSheetDismiss = { showFabSheet = false },
            showFabSheet = showFabSheet
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    showFabSheet: Boolean,
    onFabSheetDismiss: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.OrgSelector.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.OrgSelector.route) {
            OrganizationSelectorScreen(
                onOrgSelected = { orgSlug ->
                    navController.navigate(Screen.WsSelector.createRoute(orgSlug))
                }
            )
        }

        composable(
            route = Screen.WsSelector.route,
            arguments = listOf(navArgument("orgSlug") { type = NavType.StringType })
        ) {
            WorkspaceSelectorScreen(
                onWsSelected = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProductos = { navController.navigate(Screen.Productos.route) },
                onNavigateToMovimientos = { navController.navigate(Screen.Movimientos.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }

        composable(Screen.Productos.route) {
            ProductosScreen(
                onNavigateToForm = { id ->
                    navController.navigate(Screen.ProductoForm.createRoute(id))
                },
                onNavigateToCategorias = { navController.navigate(Screen.Categorias.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProductoForm.route,
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            ProductoFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Categorias.route) {
            CategoriasScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Movimientos.route) {
            MovimientosScreen(
                onNavigateToForm = { tipo ->
                    navController.navigate(Screen.MovimientoForm.createRoute(tipo))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.MovimientoForm.route,
            arguments = listOf(navArgument("tipo") { type = NavType.StringType })
        ) {
            MovimientoFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Ventas.route) {
            VentasScreen(
                onNavigateToForm = { navController.navigate(Screen.VentaForm.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.VentaForm.route) {
            VentaFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Clientes.route) {
            ClientesScreen(
                onNavigateToForm = { id ->
                    navController.navigate(Screen.ClienteForm.createRoute(id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ClienteForm.route,
            arguments = listOf(
                navArgument("clienteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            ClienteFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
    }

    if (showFabSheet) {
        ModalBottomSheet(
            onDismissRequest = onFabSheetDismiss,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            NuevoBottomSheet(
                onNuevoProducto = {
                    onFabSheetDismiss()
                    navController.navigate(Screen.ProductoForm.createRoute(null))
                },
                onNuevoMovimiento = {
                    onFabSheetDismiss()
                },
                onNuevaVenta = {
                    onFabSheetDismiss()
                    navController.navigate(Screen.VentaForm.route)
                }
            )
        }
    }
}
