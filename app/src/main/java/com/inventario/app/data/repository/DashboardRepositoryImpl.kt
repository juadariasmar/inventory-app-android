package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.DashboardApi
import com.inventario.app.domain.model.DashboardMetrics
import com.inventario.app.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepositoryImpl @Inject constructor(
    private val dashboardApi: DashboardApi,
    private val workspaceDataStore: WorkspaceDataStore
) : DashboardRepository {

    override fun getDashboardMetrics(): Flow<DashboardMetrics> = flow {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(
                DashboardMetrics(
                    totalProductos = 0,
                    totalUnidades = 0,
                    stockBajo = 0,
                    valorInventario = 0.0,
                    ultimosMovimientos = emptyList()
                )
            )
            return@flow
        }

        val response = dashboardApi.getDashboardMetrics(orgSlug, wsSlug)
        emit(response.toDomain())
    }
}
