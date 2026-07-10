package com.inventario.app.domain.usecase.producto

import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductosListUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(): Flow<List<Producto>> = repository.getProductos()
}
