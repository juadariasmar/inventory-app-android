package com.inventario.app.domain.usecase.producto

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.ProductoRepository
import javax.inject.Inject

class CreateProductoUseCase @Inject constructor(
    private val repository: ProductoRepository
) {
    suspend operator fun invoke(producto: Producto): Result<Producto> {
        if (producto.nombre.isBlank()) return Result.failure(DomainError.Validation("nombre", "Nombre es requerido"))
        if (producto.codigo.isBlank()) return Result.failure(DomainError.Validation("codigo", "Código es requerido"))
        if (producto.precio < 0) return Result.failure(DomainError.Validation("precio", "Precio no puede ser negativo"))
        if (producto.cantidad < 0) return Result.failure(DomainError.Validation("cantidad", "Cantidad no puede ser negativa"))
        return repository.createProducto(producto)
    }
}
