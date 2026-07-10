package com.inventario.app.domain.usecase.movimiento

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.repository.MovimientoRepository
import com.inventario.app.domain.repository.ProductoRepository
import javax.inject.Inject

class RegistrarMovimientoUseCase @Inject constructor(
    private val repository: MovimientoRepository,
    private val productoRepository: ProductoRepository
) {
    suspend operator fun invoke(
        productoId: Int,
        tipo: TipoMovimiento,
        cantidad: Int,
        notas: String?
    ): Result<Movimiento> {
        if (cantidad <= 0) return Result.failure(DomainError.Validation("cantidad", "La cantidad debe ser mayor a 0"))

        if (tipo == TipoMovimiento.salida) {
            val producto = productoRepository.getProductoById(productoId)
                .getOrNull()
                ?: return Result.failure(DomainError.NotFound("Producto no encontrado"))

            if (producto.cantidad < cantidad) {
                return Result.failure(
                    DomainError.Validation("cantidad", "Stock insuficiente (${producto.cantidad} disponible)")
                )
            }
        }

        return repository.registrarMovimiento(productoId, tipo, cantidad, notas)
    }
}
