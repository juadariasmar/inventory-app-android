package com.inventario.app.ui.categoria

import com.inventario.app.domain.model.Categoria

data class CategoriasUiState(
    val isLoading: Boolean = false,
    val categorias: List<Categoria> = emptyList(),
    val productoCountByCategoria: Map<Int, Int> = emptyMap(),
    val error: String? = null
)

data class CategoriaFormUiState(
    val isLoading: Boolean = false,
    val nombre: String = "",
    val prefijo: String = "",
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)
