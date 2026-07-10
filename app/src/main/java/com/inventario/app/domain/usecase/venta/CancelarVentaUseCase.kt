package com.inventario.app.domain.usecase.venta

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.repository.VentaRepository
import javax.inject.Inject

class CancelarVentaUseCase @Inject constructor(
    private val repository: VentaRepository
) {
    suspend operator fun invoke(id: Int, motivo: String): Result<Unit> {
        if (motivo.isBlank()) return Result.failure(DomainError.Validation("motivo", "El motivo de cancelación es requerido"))
        return repository.cancelarVenta(id, motivo)
    }
}
