package com.inventario.app.domain.usecase.cliente

import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.repository.ClienteRepository
import javax.inject.Inject

class GetClienteByIdUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    suspend operator fun invoke(id: Int): Result<Cliente> = repository.getClienteById(id)
}
