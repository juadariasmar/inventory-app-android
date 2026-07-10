package com.inventario.app.ui.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.usecase.cliente.GetClientesUseCase
import com.inventario.app.domain.usecase.producto.GetProductosUseCase
import com.inventario.app.domain.usecase.venta.CreateVentaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaFormViewModel @Inject constructor(
    private val getClientes: GetClientesUseCase,
    private val getProductos: GetProductosUseCase,
    private val createVenta: CreateVentaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VentaFormUiState())
    val uiState: StateFlow<VentaFormUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent

    init {
        loadClientes()
        loadProductos()
    }

    fun onClienteSelected(cliente: Cliente?) { _uiState.update { it.copy(selectedCliente = cliente) } }

    fun onAgregarItem(producto: Producto, cantidad: Int, precio: Double) {
        val items = _uiState.value.items.toMutableList()
        items.add(ItemVentaForm(producto = producto, cantidad = cantidad, precioUnitario = precio))
        _uiState.update { it.copy(items = items) }
    }

    fun onRemoverItem(index: Int) {
        val items = _uiState.value.items.toMutableList()
        items.removeAt(index)
        _uiState.update { it.copy(items = items) }
    }

    fun onNotasChanged(notas: String) { _uiState.update { it.copy(notas = notas) } }

    val total: Double get() = _uiState.value.items.sumOf { it.subtotal }

    fun onRegistrar() {
        viewModelScope.launch {
            if (_uiState.value.items.isEmpty()) return@launch
            _uiState.update { it.copy(isLoading = true) }
            createVenta(
                clienteId = _uiState.value.selectedCliente?.id,
                items = _uiState.value.items.map { Pair(it.producto.id, it.cantidad) },
                notas = _uiState.value.notas.ifBlank { null }
            )
                .onSuccess { _navigationEvent.emit(true) }
                .onFailure { _uiState.update { it.copy(isLoading = false, error = it.message) } }
        }
    }

    fun onDismissError() { _uiState.update { it.copy(error = null) } }

    private fun loadClientes() {
        viewModelScope.launch {
            getClientes()
                .catch { /* silent */ }
                .collect { clientes -> _uiState.update { it.copy(clientes = clientes) } }
        }
    }

    private fun loadProductos() {
        viewModelScope.launch {
            getProductos()
                .catch { /* silent */ }
                .collect { productos -> _uiState.update { it.copy(productos = productos) } }
        }
    }
}
