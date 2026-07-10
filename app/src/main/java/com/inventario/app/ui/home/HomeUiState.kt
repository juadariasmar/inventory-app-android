package com.inventario.app.ui.home

import com.inventario.app.domain.model.DashboardMetrics

data class HomeUiState(
    val isLoading: Boolean = false,
    val metrics: DashboardMetrics? = null,
    val error: String? = null
)
