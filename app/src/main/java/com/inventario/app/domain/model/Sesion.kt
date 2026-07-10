package com.inventario.app.domain.model

data class Sesion(
    val userId: String,
    val email: String,
    val nombre: String,
    val rol: String,
    val estado: String,
    val permisos: List<String>
) {
    fun esAdmin(): Boolean = rol in listOf("SUPER_ADMIN", "ADMIN")
    fun puedeRegistrarMovimientos(): Boolean =
        esAdmin() || permisos.contains("REGISTRAR_MOVIMIENTOS")
    fun puedeRealizarVentas(): Boolean =
        esAdmin() || permisos.contains("REALIZAR_VENTAS")
    fun puedeVerAnalisis(): Boolean =
        esAdmin() || permisos.contains("VER_ANALISIS")
}
