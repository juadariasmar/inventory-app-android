package com.inventario.app.domain.usecase.producto

import com.inventario.app.domain.repository.ProductoRepository
import javax.inject.Inject

class DeleteProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int): Result<Unit> = repository.deleteProducto(id)
}
