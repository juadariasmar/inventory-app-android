package com.inventario.app.ui.venta

import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.Venta

data class VentasUiState(
    val isLoading: Boolean = false,
    val ventas: List<Venta> = emptyList(),
    val error: String? = null
)

data class VentaFormUiState(
    val isLoading: Boolean = false,
    val clientes: List<Cliente> = emptyList(),
    val productos: List<Producto> = emptyList(),
    val selectedCliente: Cliente? = null,
    val items: List<ItemVentaForm> = emptyList(),
    val notas: String = "",
    val error: String? = null
)

data class ItemVentaForm(
    val producto: Producto,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}
