package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.DashboardDto
import com.inventario.app.domain.model.DashboardMetrics

fun DashboardDto.toDomain(): DashboardMetrics {
    return DashboardMetrics(
        totalProductos = inventarioGeneral?.totalProductos ?: 0,
        totalUnidades = inventarioGeneral?.totalUnidades ?: 0,
        stockBajo = inventarioGeneral?.stockBajo ?: 0,
        valorInventario = inventarioGeneral?.valorInventario ?: 0.0,
        ultimosMovimientos = emptyList()
    )
}
