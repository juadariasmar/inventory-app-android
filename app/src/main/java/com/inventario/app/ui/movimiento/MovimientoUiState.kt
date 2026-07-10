package com.inventario.app.ui.movimiento

import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.TipoMovimiento

data class MovimientosUiState(
    val isLoading: Boolean = false,
    val movimientos: List<Movimiento> = emptyList(),
    val error: String? = null,
    val filtroTipo: TipoMovimiento? = null
)

data class MovimientoFormUiState(
    val isLoading: Boolean = false,
    val productos: List<Producto> = emptyList(),
    val selectedProducto: Producto? = null,
    val tipo: TipoMovimiento = TipoMovimiento.entrada,
    val cantidad: String = "",
    val notas: String = "",
    val error: String? = null,
    val fieldErrors: Map<String, String> = emptyMap()
)
