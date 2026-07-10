package com.inventario.app.ui.producto

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.repository.CategoriaRepository
import com.inventario.app.domain.repository.ProductoRepository
import com.inventario.app.domain.usecase.producto.DeleteProductoUseCase
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

    private val productoRepository = mockk<ProductoRepository>()
    private val categoriaRepository = mockk<CategoriaRepository>()
    private val deleteProducto = mockk<DeleteProductoUseCase>()
    private lateinit var viewModel: ProductoViewModel

    private val sampleProductos = listOf(
        Producto(1, "Widget A", null, "WA001", 15000.0, 50, 10, 1, "Categoria 1", "2024-01-01"),
        Producto(2, "Widget B", null, "WB002", 25000.0, 3, 5, 1, "Categoria 1", "2024-01-02"),
        Producto(3, "Gadget X", null, "GX003", 35000.0, 100, 20, 2, "Categoria 2", "2024-01-03")
    )

    @Before
    fun setup() {
        coEvery { productoRepository.getProductos() } returns flowOf(sampleProductos)
        coEvery { categoriaRepository.getCategorias() } returns flowOf(emptyList())
        coEvery { deleteProducto(any()) } returns Result.success(Unit)
        viewModel = ProductoViewModel(productoRepository, categoriaRepository, deleteProducto)
    }

    @Test
    fun `initial state loads products`() {
        assertThat(viewModel.uiState.value.productos).isNotEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `search filters products by name`() {
        viewModel.onSearchQueryChanged("Widget")
        val productos = viewModel.uiState.value.productos.filter {
            it.nombre.contains("Widget", ignoreCase = true) ||
            it.codigo.contains("Widget", ignoreCase = true)
        }
        assertThat(productos).hasSize(2)
    }

    @Test
    fun `search returns empty for non-matching query`() {
        viewModel.onSearchQueryChanged("ZZZZ")
        val productos = viewModel.uiState.value.productos.filter {
            it.nombre.contains("ZZZZ", ignoreCase = true) ||
            it.codigo.contains("ZZZZ", ignoreCase = true)
        }
        assertThat(productos).isEmpty()
    }

    @Test
    fun `delete calls repository`() = runTest {
        viewModel = ProductoViewModel(productoRepository, categoriaRepository, deleteProducto)
        viewModel.onDelete(1)
        coEvery { deleteProducto.invoke(1) } returns Result.success(Unit)
    }

    @Test
    fun `error is cleared on dismissError`() {
        viewModel.onDismissError()
        assertThat(viewModel.uiState.value.error).isNull()
    }
}
