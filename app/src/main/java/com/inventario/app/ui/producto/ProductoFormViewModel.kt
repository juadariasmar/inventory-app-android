package com.inventario.app.ui.producto

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Categoria
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.usecase.categoria.GetCategoriasUseCase
import com.inventario.app.domain.usecase.producto.CreateProductoUseCase
import com.inventario.app.domain.usecase.producto.GetProductoByIdUseCase
import com.inventario.app.domain.usecase.producto.UpdateProductoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductoFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getProductoById: GetProductoByIdUseCase,
    private val createProducto: CreateProductoUseCase,
    private val updateProducto: UpdateProductoUseCase,
    private val getCategorias: GetCategoriasUseCase
) : ViewModel() {

    private val productoId: Int = savedStateHandle["productoId"] ?: -1
    val isEditing = productoId != -1

    private val _uiState = MutableStateFlow(ProductoFormUiState(isEditing = isEditing))
    val uiState: StateFlow<ProductoFormUiState> = _uiState.asStateFlow()

    private val _categorias = MutableStateFlow<List<Categoria>>(emptyList())
    val categorias: StateFlow<List<Categoria>> = _categorias.asStateFlow()

    private val _savedSuccessfully = MutableStateFlow(false)
    val savedSuccessfully: StateFlow<Boolean> = _savedSuccessfully.asStateFlow()

    init {
        loadCategorias()
        if (isEditing) loadProducto()
    }

    fun onCodigoChanged(codigo: String) {
        _uiState.update { it.copy(codigo = codigo, fieldErrors = it.fieldErrors - "codigo") }
    }

    fun onNombreChanged(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, fieldErrors = it.fieldErrors - "nombre") }
    }

    fun onDescripcionChanged(desc: String) {
        _uiState.update { it.copy(descripcion = desc) }
    }

    fun onPrecioChanged(precio: String) {
        _uiState.update { it.copy(precio = precio, fieldErrors = it.fieldErrors - "precio") }
    }

    fun onStockMinimoChanged(stock: String) {
        _uiState.update { it.copy(stockMinimo = stock, fieldErrors = it.fieldErrors - "stockMinimo") }
    }

    fun onCategoriaSelected(id: Int) {
        _uiState.update { it.copy(categoriaId = id, fieldErrors = it.fieldErrors - "categoriaId") }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onSave() {
        viewModelScope.launch {
            val state = _uiState.value
            val errors = validate(state)

            if (errors.isNotEmpty()) {
                _uiState.update { it.copy(fieldErrors = errors) }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val producto = Producto(
                id = if (isEditing) productoId else 0,
                nombre = state.nombre.trim(),
                descripcion = state.descripcion.trim().ifBlank { null },
                codigo = state.codigo.trim(),
                precio = state.precio.toDouble(),
                cantidad = 0,
                stockMinimo = state.stockMinimo.toIntOrNull() ?: 0,
                categoriaId = state.categoriaId ?: 0,
                categoriaNombre = null,
                creadoEn = ""
            )

            val result = if (isEditing) {
                updateProducto(productoId, producto)
            } else {
                createProducto(producto)
            }

            result
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false) }
                    _savedSuccessfully.value = true
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al guardar producto"
                        )
                    }
                }
        }
    }

    private fun validate(state: ProductoFormUiState): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (state.codigo.isBlank()) {
            errors["codigo"] = "Código es requerido"
        }
        if (state.nombre.isBlank()) {
            errors["nombre"] = "Nombre es requerido"
        }
        if (state.precio.isBlank()) {
            errors["precio"] = "Precio es requerido"
        } else {
            val precio = state.precio.toDoubleOrNull()
            if (precio == null || precio < 0) {
                errors["precio"] = "Precio debe ser un número válido"
            }
        }
        if (state.categoriaId == null) {
            errors["categoriaId"] = "Categoría es requerida"
        }
        if (state.stockMinimo.isNotBlank()) {
            val stock = state.stockMinimo.toIntOrNull()
            if (stock == null || stock < 0) {
                errors["stockMinimo"] = "Stock mínimo debe ser un número entero válido"
            }
        }

        return errors
    }

    private fun loadProducto() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getProductoById(productoId)
                .onSuccess { producto ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            codigo = producto.codigo,
                            nombre = producto.nombre,
                            descripcion = producto.descripcion ?: "",
                            precio = producto.precio.toString(),
                            stockMinimo = producto.stockMinimo.toString(),
                            categoriaId = producto.categoriaId
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar producto"
                        )
                    }
                }
        }
    }

    private fun loadCategorias() {
        viewModelScope.launch {
            getCategorias()
                .catch { /* silently fail */ }
                .collect { categorias ->
                    _categorias.value = categorias
                }
        }
    }
}
