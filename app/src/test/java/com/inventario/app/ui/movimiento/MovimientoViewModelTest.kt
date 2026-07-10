package com.inventario.app.ui.movimiento

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.usecase.movimiento.GetMovimientosUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovimientoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getMovimientos = mockk<GetMovimientosUseCase>()
    private lateinit var viewModel: MovimientoViewModel

    private val sampleMovimientos = listOf(
        Movimiento(
            id = 1,
            productoId = 1,
            productoNombre = "Widget A",
            tipo = TipoMovimiento.entrada,
            cantidad = 50,
            notas = "Compra inicial",
            ventaId = null,
            ordenCompraId = null,
            creadoEn = "2024-01-15T10:30:00",
            usuarioNombre = "Juan Pérez"
        ),
        Movimiento(
            id = 2,
            productoId = 2,
            productoNombre = "Gadget X",
            tipo = TipoMovimiento.salida,
            cantidad = 10,
            notas = "Venta #123",
            ventaId = 123,
            ordenCompraId = null,
            creadoEn = "2024-01-15T14:45:00",
            usuarioNombre = "María García"
        )
    )

    @Before
    fun setup() {
        coEvery { getMovimientos(null) } returns flowOf(sampleMovimientos)
        coEvery { getMovimientos(TipoMovimiento.entrada) } returns flowOf(
            sampleMovimientos.filter { it.tipo == TipoMovimiento.entrada }
        )
        coEvery { getMovimientos(TipoMovimiento.salida) } returns flowOf(
            sampleMovimientos.filter { it.tipo == TipoMovimiento.salida }
        )
        viewModel = MovimientoViewModel(getMovimientos)
    }

    @Test
    fun `initial state loads movements`() = runTest {
        assertThat(viewModel.uiState.value.movimientos).isEqualTo(sampleMovimientos)
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `initial filter is null (all)`() = runTest {
        assertThat(viewModel.uiState.value.filtroTipo).isNull()
    }

    @Test
    fun `filter changes reload with filter`() = runTest {
        viewModel.onFilterChanged(TipoMovimiento.entrada)

        assertThat(viewModel.uiState.value.filtroTipo).isEqualTo(TipoMovimiento.entrada)
        assertThat(viewModel.uiState.value.movimientos).hasSize(1)
        assertThat(viewModel.uiState.value.movimientos.first().tipo).isEqualTo(TipoMovimiento.entrada)
    }

    @Test
    fun `filter to salida shows only salidas`() = runTest {
        viewModel.onFilterChanged(TipoMovimiento.salida)

        assertThat(viewModel.uiState.value.filtroTipo).isEqualTo(TipoMovimiento.salida)
        assertThat(viewModel.uiState.value.movimientos).hasSize(1)
        assertThat(viewModel.uiState.value.movimientos.first().tipo).isEqualTo(TipoMovimiento.salida)
    }

    @Test
    fun `filter to null shows all movements`() = runTest {
        viewModel.onFilterChanged(TipoMovimiento.entrada)
        viewModel.onFilterChanged(null)

        assertThat(viewModel.uiState.value.filtroTipo).isNull()
        assertThat(viewModel.uiState.value.movimientos).hasSize(2)
    }

    @Test
    fun `error handling shows error message`() = runTest {
        coEvery { getMovimientos(null) } returns flow {
            throw RuntimeException("Failed to load")
        }
        val vm = MovimientoViewModel(getMovimientos)

        assertThat(vm.uiState.value.error).isEqualTo("Failed to load")
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `shows default error message when exception has no message`() = runTest {
        coEvery { getMovimientos(null) } returns flow {
            throw RuntimeException()
        }
        val vm = MovimientoViewModel(getMovimientos)

        assertThat(vm.uiState.value.error).isEqualTo("Error al cargar movimientos")
    }

    @Test
    fun `onDismissError clears the error`() = runTest {
        coEvery { getMovimientos(null) } returns flow {
            throw RuntimeException("Error")
        }
        val vm = MovimientoViewModel(getMovimientos)

        vm.onDismissError()

        assertThat(vm.uiState.value.error).isNull()
    }
}
