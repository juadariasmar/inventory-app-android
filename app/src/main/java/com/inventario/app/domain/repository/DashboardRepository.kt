package com.inventario.app.domain.repository

import com.inventario.app.domain.model.DashboardMetrics
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    fun getDashboardMetrics(): Flow<DashboardMetrics>
}
