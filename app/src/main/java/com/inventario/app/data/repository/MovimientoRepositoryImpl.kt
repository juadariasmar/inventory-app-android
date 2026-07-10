package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.local.db.dao.MovimientoDao
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.mapper.toEntity
import com.inventario.app.data.remote.api.MovimientoApi
import com.inventario.app.data.remote.dto.MovimientoCreateRequest
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.repository.MovimientoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovimientoRepositoryImpl @Inject constructor(
    private val movimientoApi: MovimientoApi,
    private val movimientoDao: MovimientoDao,
    private val authDataStore: AuthDataStore,
    private val workspaceDataStore: WorkspaceDataStore
) : MovimientoRepository {

    override fun getMovimientos(filtroTipo: TipoMovimiento?): Flow<List<Movimiento>> = flow {
        val token = authDataStore.token.first()
            ?: throw DomainError.Unauthorized
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(emptyList())
            return@flow
        }

        val cached = if (filtroTipo != null) {
            movimientoDao.getByTipo(filtroTipo.name.lowercase()).first()
        } else {
            movimientoDao.getAll().first()
        }
        emit(cached.map { it.toDomain() })

        try {
            val response = movimientoApi.getMovimientos(token, orgSlug, wsSlug)
            val entities = response.movimientos.map { it.toEntity(wsId) }
            if (filtroTipo == null) {
                movimientoDao.deleteAll()
                for (entity in entities) {
                    movimientoDao.insert(entity)
                }
            }
        } catch (_: Exception) {
        }

        val daoFlow = if (filtroTipo != null) {
            movimientoDao.getByTipo(filtroTipo.name.lowercase())
        } else {
            movimientoDao.getAll()
        }
        emitAll(daoFlow.map { entities -> entities.map { it.toDomain() } })
    }

    override suspend fun registrarMovimiento(
        productoId: Int,
        tipo: TipoMovimiento,
        cantidad: Int,
        notas: String?
    ): Result<Movimiento> = runCatching {
        val token = authDataStore.token.first() ?: throw DomainError.Unauthorized
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        val request = MovimientoCreateRequest(
            productoId = productoId,
            tipo = tipo.name.lowercase(),
            cantidad = cantidad,
            notas = notas
        )

        val response = movimientoApi.registrarMovimiento(token, orgSlug, wsSlug, request)
        val entity = response.toEntity(wsId)
        movimientoDao.insert(entity)
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
}
