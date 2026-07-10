package com.inventario.app.ui.cliente

import com.inventario.app.domain.model.Cliente

data class ClientesUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

data class ClienteFormUiState(
    val isLoading: Boolean = false,
    val nombre: String = "",
    val documento: String = "",
    val email: String = "",
    val telefono: String = "",
    val direccion: String = "",
    val notas: String = "",
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)
