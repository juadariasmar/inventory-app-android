package com.inventario.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.local.db.dao.ProductoDao
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.mapper.toEntity
import com.inventario.app.data.remote.api.ProductoApi
import com.inventario.app.data.remote.api.ProductoPagingSource
import com.inventario.app.data.remote.dto.ProductoCreateRequest
import com.inventario.app.data.remote.dto.ProductoUpdateRequest
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.ProductoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoRepositoryImpl @Inject constructor(
    private val api: ProductoApi,
    private val productoDao: ProductoDao,
    private val workspaceDataStore: WorkspaceDataStore
) : ProductoRepository {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    override fun getProductosPaged(): Flow<PagingData<Producto>> {
        val orgSlug = runBlocking { workspaceDataStore.orgSlug.first() }
        val wsSlug = runBlocking { workspaceDataStore.wsSlug.first() }

        return Pager(
            config = PagingConfig(pageSize = 20, initialLoadSize = 40, enablePlaceholders = false),
            pagingSourceFactory = { ProductoPagingSource(api, orgSlug, wsSlug) }
        ).flow.cachedIn(scope)
    }

    override fun getProductos(): Flow<List<Producto>> = flow {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(emptyList())
            return@flow
        }

        emit(productoDao.getAll().first().map { it.toDomain() })

        try {
            val response = api.getProductos(orgSlug, wsSlug)
            val entities = response.productos.map { it.toEntity(wsId) }
            productoDao.deleteAll()
            productoDao.upsertAll(entities)
        } catch (_: Exception) {
        }

        emitAll(productoDao.getAll().map { entities -> entities.map { it.toDomain() } })
    }

    override suspend fun getProductoById(id: Int): Result<Producto> = runCatching {
        val cached = productoDao.getById(id)
        if (cached != null) return@runCatching cached.toDomain()

        throw DomainError.NotFound("Producto no encontrado")
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun createProducto(producto: Producto): Result<Producto> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        val request = ProductoCreateRequest(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            codigo = producto.codigo,
            precio = producto.precio,
            cantidad = producto.cantidad,
            stockMinimo = producto.stockMinimo,
            categoriaId = producto.categoriaId
        )

        val response = api.createProducto(orgSlug, wsSlug, request)
        val entity = response.toEntity(wsId)
        productoDao.upsertAll(listOf(entity))
        response.toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Recurso no encontrado")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun updateProducto(id: Int, producto: Producto): Result<Producto> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()
        val wsId = workspaceDataStore.wsId.first()

        val request = ProductoUpdateRequest(
            nombre = producto.nombre,
            descripcion = producto.descripcion,
            codigo = producto.codigo,
            precio = producto.precio,
            cantidad = producto.cantidad,
            stockMinimo = producto.stockMinimo,
            categoriaId = producto.categoriaId
        )

        val response = api.updateProducto(orgSlug, wsSlug, id, request)
        val entity = response.toEntity(wsId)
        productoDao.upsertAll(listOf(entity))
        response.toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Producto no encontrado")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun deleteProducto(id: Int): Result<Unit> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        api.deleteProducto(orgSlug, wsSlug, id)
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Producto no encontrado")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }
}
