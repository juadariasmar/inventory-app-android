package com.inventario.app.ui.movimiento

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.usecase.movimiento.RegistrarMovimientoUseCase
import com.inventario.app.domain.usecase.producto.GetProductosUseCase
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
class MovimientoFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductos: GetProductosUseCase,
    private val registrarMovimiento: RegistrarMovimientoUseCase
) : ViewModel() {

    private val tipoInicial: String = savedStateHandle["tipo"] ?: "entrada"

    private val _uiState = MutableStateFlow(
        MovimientoFormUiState(tipo = TipoMovimiento.valueOf(tipoInicial))
    )
    val uiState: StateFlow<MovimientoFormUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent.asSharedFlow()

    init {
        loadProductos()
    }

    fun onTipoChanged(tipo: TipoMovimiento) {
        _uiState.update { it.copy(tipo = tipo, fieldErrors = emptyMap()) }
    }

    fun onProductoSelected(producto: Producto) {
        _uiState.update { it.copy(selectedProducto = producto, fieldErrors = emptyMap()) }
    }

    fun onCantidadChanged(cantidad: String) {
        _uiState.update { it.copy(cantidad = cantidad, fieldErrors = emptyMap()) }
    }

    fun onNotasChanged(notas: String) {
        _uiState.update { it.copy(notas = notas) }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onRegistrar() {
        viewModelScope.launch {
            val state = _uiState.value
            val errors = mutableMapOf<String, String>()

            if (state.selectedProducto == null) {
                errors["producto"] = "Seleccione un producto"
            }
            val cantidad = state.cantidad.toIntOrNull()
            if (cantidad == null || cantidad <= 0) {
                errors["cantidad"] = "Debe ser un número mayor a 0"
            }

            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(fieldErrors = errors) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            registrarMovimiento(
                productoId = state.selectedProducto!!.id,
                tipo = state.tipo,
                cantidad = cantidad!!,
                notas = state.notas.ifBlank { null }
            )
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _navigationEvent.emit(true)
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    private fun loadProductos() {
        viewModelScope.launch {
            getProductos()
                .catch { e ->
                    _uiState.update {
                        it.copy(error = e.message ?: "Error al cargar productos")
                    }
                }
                .collect { productos ->
                    _uiState.update { it.copy(productos = productos) }
                }
        }
    }
}
