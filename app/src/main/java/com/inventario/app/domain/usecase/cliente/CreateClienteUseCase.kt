package com.inventario.app.domain.usecase.cliente

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.repository.ClienteRepository
import javax.inject.Inject

class CreateClienteUseCase @Inject constructor(
    private val repository: ClienteRepository
) {
    suspend operator fun invoke(cliente: Cliente): Result<Cliente> {
        if (cliente.nombre.isBlank()) return Result.failure(DomainError.Validation("nombre", "Nombre es requerido"))
        return repository.createCliente(cliente)
    }
}
