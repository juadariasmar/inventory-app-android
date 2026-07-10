package com.inventario.app.ui.navigation

sealed class Screen(val route: String) {
    // Auth Flow
    object Login : Screen("login")
    object OrgSelector : Screen("org-selector")
    object WsSelector : Screen("ws-selector/{orgSlug}") {
        fun createRoute(orgSlug: String) = "ws-selector/$orgSlug"
    }

    // Main Shell
    object Home : Screen("home")
    object Productos : Screen("productos")

    // Producto
    object ProductoForm : Screen("productos/form?id={productoId}") {
        fun createRoute(productoId: Int? = null) =
            if (productoId != null) "productos/form?id=$productoId"
            else "productos/form"
    }

    // Categorias
    object Categorias : Screen("categorias")

    // Movimientos
    object Movimientos : Screen("movimientos")
    object MovimientoForm : Screen("movimientos/form?tipo={tipo}") {
        fun createRoute(tipo: String) = "movimientos/form?tipo=$tipo"
    }

    // Ventas
    object Ventas : Screen("ventas")
    object VentaForm : Screen("ventas/form")

    // Clientes
    object Clientes : Screen("clientes")
    object ClienteForm : Screen("clientes/form?id={clienteId}") {
        fun createRoute(clienteId: Int? = null) =
            if (clienteId != null) "clientes/form?id=$clienteId"
            else "clientes/form"
    }

    // Settings
    object Settings : Screen("settings")
    object ProfileEdit : Screen("settings/perfil")

    // Notificaciones
    object Notificaciones : Screen("notificaciones")
}
