package com.inventario.app.ui.movimiento

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.usecase.movimiento.GetMovimientosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovimientoViewModel @Inject constructor(
    private val getMovimientos: GetMovimientosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MovimientosUiState())
    val uiState: StateFlow<MovimientosUiState> = _uiState.asStateFlow()

    init {
        loadMovimientos()
    }

    fun onFilterChanged(tipo: TipoMovimiento?) {
        _uiState.update { it.copy(filtroTipo = tipo, isLoading = true) }
        loadMovimientos()
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadMovimientos()
    }

    private fun loadMovimientos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getMovimientos(_uiState.value.filtroTipo)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar movimientos"
                        )
                    }
                }
                .collect { movimientos ->
                    _uiState.update {
                        it.copy(isLoading = false, movimientos = movimientos)
                    }
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}
