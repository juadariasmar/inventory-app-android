package com.inventario.app.domain.usecase.categoria

import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.repository.CategoriaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriasUseCase @Inject constructor(
    private val repository: CategoriaRepository
) {
    operator fun invoke(): Flow<List<Categoria>> = repository.getCategorias()
}
