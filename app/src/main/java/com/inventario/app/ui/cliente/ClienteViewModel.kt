package com.inventario.app.ui.cliente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.usecase.cliente.GetClientesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val getClientes: GetClientesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClientesUiState())
    val uiState: StateFlow<ClientesUiState> = _uiState.asStateFlow()

    init { loadClientes() }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onDismissError() { _uiState.update { it.copy(error = null) } }

    private fun loadClientes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getClientes()
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { clientes ->
                    val filtered = if (_uiState.value.searchQuery.isBlank()) clientes
                    else clientes.filter {
                        it.nombre.contains(_uiState.value.searchQuery, ignoreCase = true) ||
                                it.documento?.contains(_uiState.value.searchQuery, ignoreCase = true) == true
                    }
                    _uiState.update { it.copy(isLoading = false, clientes = filtered) }
                }
        }
    }
}
