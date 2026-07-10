package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Cliente
import kotlinx.coroutines.flow.Flow

interface ClienteRepository {
    fun getClientes(): Flow<List<Cliente>>
    suspend fun getClienteById(id: Int): Result<Cliente>
    suspend fun createCliente(cliente: Cliente): Result<Cliente>
    suspend fun updateCliente(id: Int, cliente: Cliente): Result<Cliente>
}
