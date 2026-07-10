package com.inventario.app.ui.producto

import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.model.Producto

data class ProductosUiState(
    val isLoading: Boolean = false,
    val isDeleting: Boolean = false,
    val allProductos: List<Producto> = emptyList(),
    val categorias: List<Categoria> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategoriaId: Int? = null
)

data class ProductoFormUiState(
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val codigo: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val stockMinimo: String = "",
    val categoriaId: Int? = null,
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)
