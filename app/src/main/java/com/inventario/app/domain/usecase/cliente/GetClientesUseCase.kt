package com.inventario.app.domain.usecase.cliente

import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.repository.ClienteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetClientesUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    operator fun invoke(): Flow<List<Cliente>> = repository.getClientes()
}
