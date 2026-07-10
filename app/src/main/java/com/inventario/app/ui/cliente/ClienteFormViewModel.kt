package com.inventario.app.ui.cliente

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.usecase.cliente.CreateClienteUseCase
import com.inventario.app.domain.usecase.cliente.GetClienteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClienteFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createCliente: CreateClienteUseCase,
    private val getClienteById: GetClienteByIdUseCase
) : ViewModel() {

    private val clienteId: Int = savedStateHandle["clienteId"] ?: -1
    val isEditing = clienteId != -1

    private val _uiState = MutableStateFlow(ClienteFormUiState())
    val uiState: StateFlow<ClienteFormUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<Boolean>()
    val navigationEvent: SharedFlow<Boolean> = _navigationEvent

    init { if (isEditing) loadCliente() }

    fun onNombreChanged(v: String) { _uiState.update { it.copy(nombre = v) } }
    fun onDocumentoChanged(v: String) { _uiState.update { it.copy(documento = v) } }
    fun onEmailChanged(v: String) { _uiState.update { it.copy(email = v) } }
    fun onTelefonoChanged(v: String) { _uiState.update { it.copy(telefono = v) } }
    fun onDireccionChanged(v: String) { _uiState.update { it.copy(direccion = v) } }
    fun onNotasChanged(v: String) { _uiState.update { it.copy(notas = v) } }

    fun onGuardar() {
        viewModelScope.launch {
            if (_uiState.value.nombre.isBlank()) {
                _uiState.update { it.copy(fieldErrors = mapOf("nombre" to "Nombre es requerido")) }
                return@launch
            }
            _uiState.update { it.copy(isLoading = true) }
            createCliente(
                Cliente(
                    id = 0,
                    nombre = _uiState.value.nombre,
                    documento = _uiState.value.documento.ifBlank { null },
                    email = _uiState.value.email.ifBlank { null },
                    telefono = _uiState.value.telefono.ifBlank { null },
                    direccion = _uiState.value.direccion.ifBlank { null },
                    notas = _uiState.value.notas.ifBlank { null }
                )
            )
                .onSuccess { _navigationEvent.emit(true) }
                .onFailure { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
        }
    }

    fun onDismissError() { _uiState.update { it.copy(error = null) } }

    private fun loadCliente() {
        viewModelScope.launch {
            getClienteById(clienteId)
                .onSuccess { c ->
                    _uiState.update {
                        it.copy(
                            nombre = c.nombre,
                            documento = c.documento.orEmpty(),
                            email = c.email.orEmpty(),
                            telefono = c.telefono.orEmpty(),
                            direccion = c.direccion.orEmpty(),
                            notas = c.notas.orEmpty()
                        )
                    }
                }
        }
    }
}
