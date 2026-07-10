package com.inventario.app.domain.model

data class DashboardMetrics(
    val totalProductos: Int,
    val totalUnidades: Int,
    val stockBajo: Int,
    val valorInventario: Double,
    val ultimosMovimientos: List<Movimiento>
)
