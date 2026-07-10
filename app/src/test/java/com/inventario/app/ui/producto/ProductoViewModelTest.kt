package com.inventario.app.ui.producto

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.usecase.categoria.GetCategoriasUseCase
import com.inventario.app.domain.usecase.producto.DeleteProductoUseCase
import com.inventario.app.domain.usecase.producto.GetProductosListUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductos = mockk<GetProductosListUseCase>()
    private val getCategorias = mockk<GetCategoriasUseCase>()
    private val deleteProducto = mockk<DeleteProductoUseCase>()
    private lateinit var viewModel: ProductoViewModel

    private val sampleProductos = listOf(
        Producto(1, "Widget A", null, "WA001", 15000.0, 50, 10, 1, "Categoría 1", "2024-01-01"),
        Producto(2, "Widget B", null, "WB002", 25000.0, 3, 5, 1, "Categoría 1", "2024-01-02"),
        Producto(3, "Gadget X", null, "GX003", 35000.0, 100, 20, 2, "Categoría 2", "2024-01-03")
    )

    @Before
    fun setup() {
        coEvery { getProductos() } returns flowOf(emptyList())
        coEvery { getCategorias() } returns flowOf(emptyList())
        viewModel = ProductoViewModel(getProductos, getCategorias, deleteProducto)
    }

    @Test
    fun `initial state loads products`() = runTest {
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        assertThat(vm.uiState.value.allProductos).isEqualTo(sampleProductos)
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `search filters products by name`() = runTest {
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        vm.onSearchQueryChanged("Widget")

        assertThat(vm.uiState.value.searchQuery).isEqualTo("Widget")
    }

    @Test
    fun `category filter updates state`() = runTest {
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        vm.onCategoryFilterChanged(1)

        assertThat(vm.uiState.value.selectedCategoriaId).isEqualTo(1)
    }

    @Test
    fun `delete calls repository`() = runTest {
        coEvery { deleteProducto(1) } returns Result.success(Unit)
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        vm.onDelete(1)

        assertThat(vm.uiState.value.isDeleting).isFalse()
    }

    @Test
    fun `delete shows error on failure`() = runTest {
        coEvery { deleteProducto(1) } returns Result.failure(RuntimeException("Error"))
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        vm.onDelete(1)

        assertThat(vm.uiState.value.error).isEqualTo("Error")
    }

    @Test
    fun `onDismissError clears error`() = runTest {
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = ProductoViewModel(getProductos, getCategorias, deleteProducto)

        vm.onDelete(1)
        vm.onDismissError()

        assertThat(vm.uiState.value.error).isNull()
    }
}
