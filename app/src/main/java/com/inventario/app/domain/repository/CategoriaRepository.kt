package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Categoria
import kotlinx.coroutines.flow.Flow

interface CategoriaRepository {
    fun getCategorias(): Flow<List<Categoria>>
    suspend fun createCategoria(categoria: Categoria): Result<Categoria>
    suspend fun updateCategoria(id: Int, categoria: Categoria): Result<Categoria>
}
