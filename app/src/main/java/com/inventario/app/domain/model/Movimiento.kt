package com.inventario.app.domain.model

data class Movimiento(
    val id: Int,
    val productoId: Int,
    val productoNombre: String?,
    val tipo: TipoMovimiento,
    val cantidad: Int,
    val notas: String?,
    val ventaId: Int?,
    val ordenCompraId: Int?,
    val creadoEn: String,
    val usuarioNombre: String?
)
