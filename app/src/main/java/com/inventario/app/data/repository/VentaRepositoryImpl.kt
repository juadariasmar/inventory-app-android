package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.VentaApi
import com.inventario.app.data.remote.dto.CancelarVentaRequest
import com.inventario.app.data.remote.dto.VentaCreateRequest
import com.inventario.app.data.remote.dto.VentaItemRequest
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Venta
import com.inventario.app.domain.repository.VentaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VentaRepositoryImpl @Inject constructor(
    private val ventaApi: VentaApi,
    private val workspaceDataStore: WorkspaceDataStore
) : VentaRepository {

    override fun getVentas(): Flow<List<Venta>> = flow {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(emptyList())
            return@flow
        }

        val ventas = ventaApi.getVentas(orgSlug, wsSlug)
        emit(ventas.map { it.toDomain() })
    }

    override suspend fun getVentaById(id: Int): Result<Venta> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        val ventas = ventaApi.getVentas(orgSlug, wsSlug)
        ventas.firstOrNull { it.id == id }?.toDomain()
            ?: throw DomainError.NotFound("Venta no encontrada")
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun createVenta(
        clienteId: Int?,
        items: List<Pair<Int, Int>>,
        notas: String?
    ): Result<Venta> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        val request = VentaCreateRequest(
            clienteId = clienteId,
            items = items.map { (productoId, cantidad) ->
                VentaItemRequest(productoId = productoId, cantidad = cantidad)
            },
            notas = notas
        )

        val response = ventaApi.createVenta(orgSlug, wsSlug, request)
        response.toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun cancelarVenta(id: Int, motivo: String): Result<Unit> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        ventaApi.cancelarVenta(orgSlug, wsSlug, id, CancelarVentaRequest(motivo))
        Unit
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Venta no encontrada")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }
}
