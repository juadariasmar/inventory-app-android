package com.inventario.app.ui.venta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.usecase.venta.GetVentasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VentaViewModel @Inject constructor(
    private val getVentas: GetVentasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(VentasUiState())
    val uiState: StateFlow<VentasUiState> = _uiState.asStateFlow()

    init { loadVentas() }

    fun onDismissError() { _uiState.update { it.copy(error = null) } }

    private fun loadVentas() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getVentas()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { ventas ->
                    _uiState.update { it.copy(isLoading = false, ventas = ventas) }
                }
        }
    }
}
