package com.inventario.app.domain.usecase.venta

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Venta
import com.inventario.app.domain.repository.VentaRepository
import javax.inject.Inject

class CreateVentaUseCase @Inject constructor(
    private val repository: VentaRepository
) {
    suspend operator fun invoke(
        clienteId: Int?,
        items: List<Pair<Int, Int>>,
        notas: String?
    ): Result<Venta> {
        if (items.isEmpty()) return Result.failure(DomainError.Validation("items", "Debe agregar al menos un producto"))
        items.forEach { (productoId, cantidad) ->
            if (cantidad <= 0) return Result.failure(
                DomainError.Validation("cantidad", "La cantidad del producto $productoId debe ser mayor a 0")
            )
        }
        return repository.createVenta(clienteId, items, notas)
    }
}
