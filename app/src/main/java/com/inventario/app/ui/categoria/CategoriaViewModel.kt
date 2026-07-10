package com.inventario.app.ui.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.usecase.categoria.CreateCategoriaUseCase
import com.inventario.app.domain.usecase.categoria.GetCategoriasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriaViewModel @Inject constructor(
    private val getCategorias: GetCategoriasUseCase,
    private val createCategoria: CreateCategoriaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriasUiState())
    val uiState: StateFlow<CategoriasUiState> = _uiState.asStateFlow()

    private val _formUiState = MutableStateFlow(CategoriaFormUiState())
    val formUiState: StateFlow<CategoriaFormUiState> = _formUiState.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private val _createdSuccessfully = MutableStateFlow(false)
    val createdSuccessfully: StateFlow<Boolean> = _createdSuccessfully.asStateFlow()

    init {
        loadCategorias()
    }

    fun onShowDialog() {
        _showDialog.value = true
        _formUiState.value = CategoriaFormUiState()
    }

    fun onDismissDialog() {
        _showDialog.value = false
        _formUiState.value = CategoriaFormUiState()
    }

    fun onFormNombreChanged(nombre: String) {
        _formUiState.update { it.copy(nombre = nombre, fieldErrors = it.fieldErrors - "nombre") }
    }

    fun onFormPrefijoChanged(prefijo: String) {
        _formUiState.update { it.copy(prefijo = prefijo, fieldErrors = it.fieldErrors - "prefijo") }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onCreateCategoria() {
        viewModelScope.launch {
            val formState = _formUiState.value
            val errors = validateForm(formState)

            if (errors.isNotEmpty()) {
                _formUiState.update { it.copy(fieldErrors = errors) }
                return@launch
            }

            _formUiState.update { it.copy(isLoading = true, error = null) }

            createCategoria(
                Categoria(
                    id = 0,
                    nombre = formState.nombre.trim(),
                    prefijo = formState.prefijo.trim()
                )
            )
                .onSuccess {
                    _formUiState.update { it.copy(isLoading = false) }
                    _showDialog.value = false
                    _createdSuccessfully.value = true
                }
                .onFailure { e ->
                    _formUiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al crear categoría"
                        )
                    }
                }
        }
    }

    private fun validateForm(state: CategoriaFormUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()
        if (state.nombre.isBlank()) {
            errors["nombre"] = "Nombre es requerido"
        }
        if (state.prefijo.isBlank()) {
            errors["prefijo"] = "Prefijo es requerido"
        } else if (state.prefijo.length > 10) {
            errors["prefijo"] = "Prefijo no puede tener más de 10 caracteres"
        }
        return errors
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            getCategorias()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar categorías"
                        )
                    }
                }
                .collect { categorias ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            categorias = categorias,
                            error = null
                        )
                    }
                }
        }
    }
}
