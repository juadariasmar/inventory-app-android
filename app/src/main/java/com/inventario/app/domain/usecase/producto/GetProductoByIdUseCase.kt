package com.inventario.app.domain.usecase.producto

import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.ProductoRepository
import javax.inject.Inject

class GetProductoByIdUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(id: Int): Result<Producto> = repository.getProductoById(id)
}
