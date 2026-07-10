package com.inventario.app.domain.usecase.producto

import androidx.paging.PagingData
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.ProductoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductosUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    operator fun invoke(): Flow<PagingData<Producto>> = repository.getProductosPaged()
}
