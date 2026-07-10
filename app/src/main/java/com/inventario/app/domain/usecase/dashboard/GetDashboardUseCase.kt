package com.inventario.app.domain.usecase.dashboard

import com.inventario.app.domain.model.DashboardMetrics
import com.inventario.app.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDashboardUseCase @Inject constructor(
    private val dashboardRepository: DashboardRepository
) {
    operator fun invoke(): Flow<DashboardMetrics> {
        return dashboardRepository.getDashboardMetrics()
    }
}
