# Flujo de Navegacion — InventarioApp Android

---

## 1. Diagrama de Grafo de Navegacion

```
                              ┌───────────┐
                              │   Splash  │
                              │  Screen   │
                              └─────┬─────┘
                                    │
                           ┌────────▼────────┐
                           │   TokenSaved?   │
                           └────────┬────────┘
                            NO      │      SÍ
                  ┌─────────────────┤─────────────────┐
                  ▼                                   ▼
           ┌─────────────┐                   ┌──────────────┐
           │    Login    │                   │  Validar     │
           │    Screen   │                   │  Token       │
           └──────┬──────┘                   └──────┬───────┘
                  │                                 │
                  │                    ┌────────────┴────────────┐
                  │                 Valido                   Invalido
                  │                    │                         │
                  │                    │                         ▼
                  │                    │                  ┌─────────────┐
                  │                    │                  │    Login    │
                  │                    │                  │    Screen   │
                  │                    │                  └──────┬──────┘
                  │                    │                         │
                  ▼                    ▼                         │
           ┌──────────────────────────────┐                     │
           │    Org Selector Screen       │◄────────────────────┘
           └──────────────┬───────────────┘
                          │
                  ┌───────▼───────┐
                  │ Orgs > 1?     │
                  └───────┬───────┘
                   SÍ     │     NO
             ┌────────────┤────────────┐
             ▼                         ▼
      ┌──────────────┐        ┌──────────────┐
      │  Org Selector│        │  WS Selector │
      │  Screen      │        │  Screen      │
      └──────┬───────┘        └──────┬───────┘
             │                        │
             ▼                        │
      ┌──────────────┐                │
      │  WS Selector │◄───────────────┘
      │  Screen      │
      └──────┬───────┘
             │
             ▼
  ┌──────────────────────────────────┐
  │        MainAppShell              │
  │  (BottomNav + NavHost)           │
  │                                  │
  │  ┌──────┐  ┌──────┐  ┌──────┐  │
  │  │ Home │→ │Prod. │→ │Form  │  │
  │  │      │  │List  │  │Prod. │  │
  │  └──┬───┘  └──┬───┘  └──────┘  │
  │     │    ┌────▼─────┐           │
  │     │    │ Categorias│           │
  │     │    └──────────┘           │
  │  ┌──┴──────┐  ┌──────────┐     │
  │  │Movim.   │  │ Settings │     │
  │  │List     │  │          │     │
  │  └──┬──────┘  └──────────┘     │
  │     │                           │
  │  ┌──▼────────┐  ┌──────────┐   │
  │  │Movimiento │  │  Ventas  │   │
  │  │Form       │  │  List    │   │
  │  └───────────┘  └──┬───────┘   │
  │                     │           │
  │                 ┌───▼───────┐   │
  │                 │ Venta     │   │
  │                 │ Form      │   │
  │                 └───────────┘   │
  └──────────────────────────────────┘
```

---

## 2. Rutas Tipadas (Typed Navigation)

```kotlin
sealed class Screen(val route: String) {

    // ── Auth Flow ──
    object Login : Screen("login")
    object OrgSelector : Screen("org-selector")
    object WsSelector : Screen("ws-selector/{orgSlug}") {
        fun createRoute(orgSlug: String) = "ws-selector/$orgSlug"
    }

    // ── Main Shell ──
    object Home : Screen("home")
    object Productos : Screen("productos")

    // ── Producto ──
    object ProductoForm : Screen("productos/form?id={productoId}") {
        fun createRoute(productoId: Int? = null) =
            if (productoId != null) "productos/form?id=$productoId"
            else "productos/form"
    }

    // ── Categorias ──
    object Categorias : Screen("categorias")

    // ── Movimientos ──
    object Movimientos : Screen("movimientos")
    object MovimientoForm : Screen("movimientos/form?tipo={tipo}") {
        fun createRoute(tipo: String) = "movimientos/form?tipo=$tipo"
    }

    // ── Ventas ──
    object Ventas : Screen("ventas")
    object VentaForm : Screen("ventas/form")

    // ── Clientes ──
    object Clientes : Screen("clientes")
    object ClienteForm : Screen("clientes/form?id={clienteId}") {
        fun createRoute(clienteId: Int? = null) =
            if (clienteId != null) "clientes/form?id=$clienteId"
            else "clientes/form"
    }

    // ── Settings ──
    object Settings : Screen("settings")
}
```

---

## 3. NavHost Configuration

```kotlin
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ── Auth Flow ──
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
        ) { backStackEntry ->
            val orgSlug = backStackEntry.arguments?.getString("orgSlug") ?: return@composable
            WorkspaceSelectorScreen(
                orgSlug = orgSlug,
                onWsSelected = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // ── Main Shell ──
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
                onNavigateToDetail = { id -> /*TODO*/ },
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
}
```

---

## 4. Reglas de Navegacion

| # | Regla | Implementacion |
|---|-------|---------------|
| 1 | Login → Org Selector | `popUpTo(Login) { inclusive = true }` |
| 2 | Si solo 1 org → saltar a WS Selector | `if (orgs.size == 1) navigate(WsSelector)` |
| 3 | WS Selector → Home | `popUpTo(Login) { inclusive = true }` |
| 4 | Back from Home → confirm exit | `BackHandler` + `AlertDialog` |
| 5 | Logout → Login (clear all) | `navigate(Login) { popUpTo(0) { inclusive = true } }` |
| 6 | Token expired → Login | `AuthInterceptor` detecta 401 → `navigate(Login)` |
| 7 | BottomNav → no back stack | `popUpTo(home) { saveState = true; launchSingleTop = true }` |
| 8 | FAB "Nuevo" → BottomSheet | `ModalBottomSheet` con opciones |

---

## 5. Bottom Navigation Configuration

```kotlin
@Composable
fun InventarioBottomNav(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onFabClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem("Home", Icons.Filled.Home, Screen.Home.route),
        BottomNavItem("Productos", Icons.Filled.Inventory2, Screen.Productos.route),
        BottomNavItem("Nuevo", Icons.Filled.Add, ""), // FAB, no route
        BottomNavItem("Movimientos", Icons.Filled.SwapHoriz, Screen.Movimientos.route),
        BottomNavItem("Config", Icons.Filled.Settings, Screen.Settings.route)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            if (index == 2) {
                // FAB center item
                FloatingActionButton(
                    onClick = onFabClick,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Nuevo")
                }
            } else {
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) },
                    selected = currentRoute == item.route,
                    onClick = { onNavigate(item.route) }
                )
            }
        }
    }
}
```

---

## 6. FAB Bottom Sheet

```kotlin
@Composable
fun NuevoBottomSheet(
    onDismiss: () -> Unit,
    onNuevoProducto: () -> Unit,
    onNuevoMovimiento: () -> Unit,
    onNuevaVenta: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.padding(16.dp)) {
            ListItem(
                headlineContent = { Text("Nuevo Producto") },
                supportingContent = { Text("Crear producto nuevo en el inventario") },
                leadingContent = { Icon(Icons.Filled.Inventory2, null) },
                modifier = Modifier.clickable {
                    onDismiss()
                    onNuevoProducto()
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Nuevo Movimiento") },
                supportingContent = { Text("Registrar entrada o salida de stock") },
                leadingContent = { Icon(Icons.Filled.SwapHoriz, null) },
                modifier = Modifier.clickable {
                    onDismiss()
                    onNuevoMovimiento()
                }
            )
            HorizontalDivider()
            ListItem(
                headlineContent = { Text("Nueva Venta") },
                supportingContent = { Text("Crear venta con uno o mas productos") },
                leadingContent = { Icon(Icons.Filled.PointOfSale, null) },
                modifier = Modifier.clickable {
                    onDismiss()
                    onNuevaVenta()
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
```
