package com.inventario.app.ui.venta

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.ItemVenta
import com.inventario.app.domain.model.Venta
import com.inventario.app.domain.usecase.venta.GetVentasUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class VentaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getVentas = mockk<GetVentasUseCase>()
    private lateinit var viewModel: VentaViewModel

    private val sampleVentas = listOf(
        Venta(
            id = 1,
            vendedorNombre = "Vendedor",
            clienteNombre = "Cliente A",
            total = 350.0,
            notas = null,
            items = listOf(
                ItemVenta(1, 1, "Widget", 2, 100.0, 200.0),
                ItemVenta(2, 2, "Gadget", 3, 50.0, 150.0)
            ),
            canceladaEn = null,
            motivoCancelacion = null,
            creadoEn = "2024-01-15T10:30:00Z"
        ),
        Venta(
            id = 2,
            vendedorNombre = "Vendedor",
            clienteNombre = "Cliente B",
            total = 500.0,
            notas = "Nota",
            items = listOf(
                ItemVenta(3, 3, "Producto X", 1, 500.0, 500.0)
            ),
            canceladaEn = null,
            motivoCancelacion = null,
            creadoEn = "2024-01-16T14:00:00Z"
        )
    )

    @Before
    fun setup() {
        coEvery { getVentas() } returns flowOf(emptyList())
        viewModel = VentaViewModel(getVentas)
    }

    @Test
    fun `initial state loads empty list`() = runTest {
        assertThat(viewModel.uiState.value.ventas).isEmpty()
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `loads ventas from use case`() = runTest {
        coEvery { getVentas() } returns flowOf(sampleVentas)
        val vm = VentaViewModel(getVentas)

        assertThat(vm.uiState.value.ventas).hasSize(2)
        assertThat(vm.uiState.value.ventas[0].clienteNombre).isEqualTo("Cliente A")
        assertThat(vm.uiState.value.ventas[1].clienteNombre).isEqualTo("Cliente B")
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `loads single venta`() = runTest {
        val singleVenta = listOf(sampleVentas[0])
        coEvery { getVentas() } returns flowOf(singleVenta)
        val vm = VentaViewModel(getVentas)

        assertThat(vm.uiState.value.ventas).hasSize(1)
        assertThat(vm.uiState.value.ventas.first().total).isWithin(0.01).of(350.0)
    }

    @Test
    fun `error handling shows error message`() = runTest {
        coEvery { getVentas() } returns flow {
            throw RuntimeException("Failed to load ventas")
        }
        val vm = VentaViewModel(getVentas)

        assertThat(vm.uiState.value.error).isEqualTo("Failed to load ventas")
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onDismissError clears the error`() = runTest {
        coEvery { getVentas() } returns flow {
            throw RuntimeException("Error")
        }
        val vm = VentaViewModel(getVentas)

        vm.onDismissError()

        assertThat(vm.uiState.value.error).isNull()
    }

    @Test
    fun `shows default error message when exception has no message`() = runTest {
        coEvery { getVentas() } returns flow {
            throw RuntimeException()
        }
        val vm = VentaViewModel(getVentas)

        assertThat(vm.uiState.value.error).isEqualTo("Error al cargar ventas")
    }
}
