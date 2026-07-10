package com.inventario.app.domain.usecase.categoria

import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.repository.CategoriaRepository
import javax.inject.Inject

class CreateCategoriaUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    suspend operator fun invoke(categoria: Categoria): Result<Categoria> {
        if (categoria.nombre.isBlank()) return Result.failure(DomainError.Validation("nombre", "Nombre es requerido"))
        if (categoria.prefijo.isBlank()) return Result.failure(DomainError.Validation("prefijo", "Prefijo es requerido"))
        return repository.createCategoria(categoria)
    }
}
