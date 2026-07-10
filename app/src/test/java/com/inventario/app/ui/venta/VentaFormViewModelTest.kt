package com.inventario.app.ui.venta

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.usecase.cliente.GetClientesUseCase
import com.inventario.app.domain.usecase.producto.GetProductosListUseCase
import com.inventario.app.domain.usecase.venta.CreateVentaUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VentaFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getClientes = mockk<GetClientesUseCase>()
    private val getProductos = mockk<GetProductosListUseCase>()
    private val createVenta = mockk<CreateVentaUseCase>()
    private lateinit var viewModel: VentaFormViewModel

    private val sampleProductos = listOf(
        Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01"),
        Producto(2, "Gadget", null, "G1", 50.0, 30, 3, 1, "Cat", "2024-01-01")
    )

    private val sampleClientes = listOf(
        Cliente(1, "Juan", null, null, null, null, null),
        Cliente(2, "María", null, null, null, null, null)
    )

    @Before
    fun setup() {
        coEvery { getClientes() } returns flowOf(emptyList())
        coEvery { getProductos() } returns flowOf(emptyList())
        viewModel = VentaFormViewModel(getClientes, getProductos, createVenta)
    }

    @Test
    fun `initial state loads empty form`() = runTest {
        assertThat(viewModel.uiState.value.items).isEmpty()
        assertThat(viewModel.uiState.value.clientes).isEmpty()
        assertThat(viewModel.uiState.value.productos).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `loads clientes and productos from use cases`() = runTest {
        coEvery { getClientes() } returns flowOf(sampleClientes)
        coEvery { getProductos() } returns flowOf(sampleProductos)
        val vm = VentaFormViewModel(getClientes, getProductos, createVenta)

        assertThat(vm.uiState.value.clientes).hasSize(2)
        assertThat(vm.uiState.value.productos).hasSize(2)
    }

    @Test
    fun `onClienteSelected updates selected cliente`() = runTest {
        viewModel.onClienteSelected(sampleClientes[0])
        assertThat(viewModel.uiState.value.selectedCliente).isEqualTo(sampleClientes[0])
    }

    @Test
    fun `onClienteSelected with null clears selection`() = runTest {
        viewModel.onClienteSelected(sampleClientes[0])
        viewModel.onClienteSelected(null)
        assertThat(viewModel.uiState.value.selectedCliente).isNull()
    }

    @Test
    fun `onAgregarItem adds item to list`() = runTest {
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 2, 100.0)
        assertThat(viewModel.uiState.value.items).hasSize(1)
        assertThat(viewModel.uiState.value.items[0].producto.nombre).isEqualTo("Widget")
    }

    @Test
    fun `onRemoverItem removes item`() = runTest {
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 2, 100.0)
        viewModel.onRemoverItem(0)
        assertThat(viewModel.uiState.value.items).isEmpty()
    }

    @Test
    fun `onRemoverItem with multiple items removes correct index`() = runTest {
        val p1 = Producto(1, "P1", null, "1", 100.0, 50, 5, 1, "C", "2024-01-01")
        val p2 = Producto(2, "P2", null, "2", 50.0, 50, 5, 1, "C", "2024-01-01")
        viewModel.onAgregarItem(p1, 1, 100.0)
        viewModel.onAgregarItem(p2, 2, 50.0)
        viewModel.onRemoverItem(0)
        assertThat(viewModel.uiState.value.items).hasSize(1)
        assertThat(viewModel.uiState.value.items[0].producto.nombre).isEqualTo("P2")
    }

    @Test
    fun `total calculates correctly`() = runTest {
        val p1 = Producto(1, "P1", null, "1", 100.0, 50, 5, 1, "C", "2024-01-01")
        val p2 = Producto(2, "P2", null, "2", 50.0, 50, 5, 1, "C", "2024-01-01")
        viewModel.onAgregarItem(p1, 2, 100.0)
        viewModel.onAgregarItem(p2, 3, 50.0)
        assertThat(viewModel.total).isWithin(0.01).of(350.0)
    }

    @Test
    fun `total is zero with no items`() = runTest {
        assertThat(viewModel.total).isWithin(0.01).of(0.0)
    }

    @Test
    fun `total calculates single item`() = runTest {
        val p1 = Producto(1, "P1", null, "1", 100.0, 50, 5, 1, "C", "2024-01-01")
        viewModel.onAgregarItem(p1, 5, 100.0)
        assertThat(viewModel.total).isWithin(0.01).of(500.0)
    }

    @Test
    fun `onNotasChanged updates notas`() = runTest {
        viewModel.onNotasChanged("Cliente nuevo")
        assertThat(viewModel.uiState.value.notas).isEqualTo("Cliente nuevo")
    }

    @Test
    fun `onRegistrar succeeds with items`() = runTest {
        coEvery { createVenta(any(), any(), any()) } returns Result.success(mockk())
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 2, 100.0)

        viewModel.onRegistrar()
        viewModel.navigationEvent.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `onRegistrar fails without items`() = runTest {
        viewModel.onRegistrar()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        coVerify(exactly = 0) { createVenta(any(), any(), any()) }
    }

    @Test
    fun `onRegistrar shows error on failure`() = runTest {
        coEvery { createVenta(any(), any(), any()) } returns Result.failure(RuntimeException("Error al crear venta"))
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 1, 100.0)

        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.error).isEqualTo("Error al crear venta")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onDismissError clears error`() = runTest {
        coEvery { createVenta(any(), any(), any()) } returns Result.failure(RuntimeException("Error"))
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 1, 100.0)

        viewModel.onRegistrar()
        viewModel.onDismissError()

        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `onRegistrar sends selectedCliente id`() = runTest {
        coEvery { createVenta(any(), any(), any()) } returns Result.success(mockk())
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onClienteSelected(sampleClientes[0])
        viewModel.onAgregarItem(producto, 2, 100.0)

        viewModel.onRegistrar()

        coVerify { createVenta(1, listOf(Pair(1, 2)), null) }
    }

    @Test
    fun `onRegistrar sends null notas when blank`() = runTest {
        coEvery { createVenta(any(), any(), any()) } returns Result.success(mockk())
        val producto = Producto(1, "Widget", null, "W1", 100.0, 50, 5, 1, "Cat", "2024-01-01")
        viewModel.onAgregarItem(producto, 1, 100.0)
        viewModel.onNotasChanged("")

        viewModel.onRegistrar()

        coVerify { createVenta(any(), listOf(Pair(1, 1)), null) }
    }
}
