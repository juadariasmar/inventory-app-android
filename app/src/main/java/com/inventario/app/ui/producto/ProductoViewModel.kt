package com.inventario.app.ui.producto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.repository.CategoriaRepository
import com.inventario.app.domain.repository.ProductoRepository
import com.inventario.app.domain.usecase.producto.DeleteProductoUseCase
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
class ProductoViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val deleteProducto: DeleteProductoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductosUiState())
    val uiState: StateFlow<ProductosUiState> = _uiState.asStateFlow()

    init {
        loadProductos()
        loadCategorias()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategoryFilterChanged(categoriaId: Int?) {
        _uiState.update { it.copy(selectedCategoriaId = categoriaId) }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun refresh() {
        loadProductos()
    }

    fun onDelete(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleting = true) }
            deleteProducto(id)
                .onSuccess {
                    _uiState.update { it.copy(isDeleting = false) }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            error = e.message ?: "Error al eliminar producto"
                        )
                    }
                }
        }
    }

    private fun loadProductos() {
        viewModelScope.launch {
            productoRepository.getProductos()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar productos"
                        )
                    }
                }
                .collect { productos ->
                    _uiState.update {
                        it.copy(isLoading = false, allProductos = productos)
                    }
                }
        }
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias()
                .catch { /* silently fail, categories are supplementary */ }
                .collect { categorias ->
                    _uiState.update { it.copy(categorias = categorias) }
                }
        }
    }
}
