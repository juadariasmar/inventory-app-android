package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.local.db.dao.CategoriaDao
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.mapper.toEntity
import com.inventario.app.data.remote.api.CategoriaApi
import com.inventario.app.data.remote.dto.CategoriaCreateRequest
import com.inventario.app.data.remote.dto.CategoriaUpdateRequest
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoriaRepositoryImpl @Inject constructor(
    private val categoriaApi: CategoriaApi,
    private val categoriaDao: CategoriaDao,
    private val workspaceDataStore: WorkspaceDataStore
) : CategoriaRepository {

    override fun getCategorias(): Flow<List<Categoria>> = flow {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(emptyList())
            return@flow
        }

        emit(categoriaDao.getAll().first().map { it.toDomain() })

        try {
            val response = categoriaApi.getCategorias(orgSlug, wsSlug)
            val entities = response.map { it.toEntity(wsId) }
            categoriaDao.deleteAll()
            categoriaDao.upsertAll(entities)
        } catch (_: Exception) {
        }

        emitAll(categoriaDao.getAll().map { entities -> entities.map { it.toDomain() } })
    }

    override suspend fun createCategoria(categoria: Categoria): Result<Categoria> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        val request = CategoriaCreateRequest(
            nombre = categoria.nombre,
            prefijo = categoria.prefijo
        )

        val response = categoriaApi.createCategoria(orgSlug, wsSlug, request)
        val entity = response.toEntity(wsId)
        categoriaDao.upsertAll(listOf(entity))
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

    override suspend fun updateCategoria(id: Int, categoria: Categoria): Result<Categoria> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        val request = CategoriaUpdateRequest(
            nombre = categoria.nombre,
            prefijo = categoria.prefijo
        )

        val response = categoriaApi.updateCategoria(orgSlug, wsSlug, id, request)
        val entity = response.toEntity(wsId)
        categoriaDao.upsertAll(listOf(entity))
        response.toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Categoría no encontrada")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }
}
