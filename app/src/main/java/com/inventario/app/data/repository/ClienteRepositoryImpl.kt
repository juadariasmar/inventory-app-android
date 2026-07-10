package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.ClienteApi
import com.inventario.app.data.remote.dto.ClienteCreateRequest
import com.inventario.app.data.remote.dto.ClienteUpdateRequest
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClienteRepositoryImpl @Inject constructor(
    private val clienteApi: ClienteApi,
    private val workspaceDataStore: WorkspaceDataStore
) : ClienteRepository {

    override fun getClientes(): Flow<List<Cliente>> = flow {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        if (orgSlug.isBlank() || wsSlug.isBlank()) {
            emit(emptyList())
            return@flow
        }

        val clientes = clienteApi.getClientes(orgSlug, wsSlug)
        emit(clientes.map { it.toDomain() })
    }

    override suspend fun getClienteById(id: Int): Result<Cliente> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        clienteApi.getClienteById(orgSlug, wsSlug, id).toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    404 -> throw DomainError.NotFound("Cliente no encontrado")
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun createCliente(cliente: Cliente): Result<Cliente> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        val request = ClienteCreateRequest(
            nombre = cliente.nombre,
            documento = cliente.documento,
            email = cliente.email,
            telefono = cliente.telefono,
            direccion = cliente.direccion,
            notas = cliente.notas
        )

        clienteApi.createCliente(orgSlug, wsSlug, request).toDomain()
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

    override suspend fun updateCliente(id: Int, cliente: Cliente): Result<Cliente> = runCatching {
        val orgSlug = workspaceDataStore.orgSlug.first()
        val wsSlug = workspaceDataStore.wsSlug.first()

        val request = ClienteUpdateRequest(
            nombre = cliente.nombre,
            documento = cliente.documento,
            email = cliente.email,
            telefono = cliente.telefono,
            direccion = cliente.direccion,
            notas = cliente.notas
        )

        clienteApi.updateCliente(orgSlug, wsSlug, id, request).toDomain()
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    403 -> throw DomainError.Forbidden
                    404 -> throw DomainError.NotFound("Cliente no encontrado")
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }
}
