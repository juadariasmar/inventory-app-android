package com.inventario.app.domain.model

enum class EstadoVenta { PENDIENTE, COMPLETADA, CANCELADA }

data class Venta(
    val id: Int,
    val vendedorNombre: String?,
    val clienteNombre: String?,
    val total: Double,
    val notas: String?,
    val items: List<ItemVenta>,
    val canceladaEn: String?,
    val motivoCancelacion: String?,
    val creadoEn: String
)

data class ItemVenta(
    val id: Int,
    val productoId: Int,
    val productoNombre: String?,
    val cantidad: Int,
    val precioUnitario: Double,
    val subtotal: Double
)
