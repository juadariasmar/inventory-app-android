package com.inventario.app.ui.producto

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.repository.CategoriaRepository
import com.inventario.app.domain.repository.ProductoRepository
import com.inventario.app.domain.usecase.producto.DeleteProductoUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepository = mockk<ProductoRepository>(relaxed = true)
    private val categoriaRepository = mockk<CategoriaRepository>(relaxed = true)
    private val deleteProducto = mockk<DeleteProductoUseCase>(relaxed = true)
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setup() {
        coEvery { productoRepository.getProductos() } returns flowOf(emptyList())
        coEvery { categoriaRepository.getCategorias() } returns flowOf(emptyList())
        viewModel = ProductoViewModel(productoRepository, categoriaRepository, deleteProducto)
    }

    @Test
    fun `initial state loads products`() {
        assertThat(viewModel.uiState.value).isNotNull()
    }

    @Test
    fun `onDismissError clears error`() {
        viewModel.onDismissError()
        assertThat(viewModel.uiState.value.error).isNull()
    }
}
