package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.DashboardDto
import com.inventario.app.domain.model.DashboardMetrics

fun DashboardDto.toDomain(): DashboardMetrics {
    val totalProductos = inventarioGeneral.size
    val totalUnidades = inventarioGeneral.sumOf { it.cantidad }
    val stockBajo = inventarioGeneral.count { it.estado == "Stock bajo" || it.estado == "Sin stock" }
    val valorInventario = inventarioGeneral.sumOf { it.valorEnStock }

    return DashboardMetrics(
        totalProductos = totalProductos,
        totalUnidades = totalUnidades,
        stockBajo = stockBajo,
        valorInventario = valorInventario,
        ultimosMovimientos = emptyList()
    )
}
