package com.inventario.app.ui.movimiento

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.Producto
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.usecase.movimiento.RegistrarMovimientoUseCase
import com.inventario.app.domain.usecase.producto.GetProductosListUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovimientoFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getProductos = mockk<GetProductosListUseCase>()
    private val registrarMovimiento = mockk<RegistrarMovimientoUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    private lateinit var viewModel: MovimientoFormViewModel

    private val sampleProductos = listOf(
        Producto(1, "Widget A", null, "WA001", 15000.0, 50, 10, 1, "Categoría 1", "2024-01-01"),
        Producto(2, "Gadget X", null, "GX003", 35000.0, 3, 5, 2, "Categoría 2", "2024-01-03")
    )

    private val sampleMovimiento = Movimiento(
        id = 1,
        productoId = 1,
        productoNombre = "Widget A",
        tipo = TipoMovimiento.entrada,
        cantidad = 50,
        notas = null,
        ventaId = null,
        ordenCompraId = null,
        creadoEn = "2024-01-15T10:30:00",
        usuarioNombre = "Test User"
    )

    @Before
    fun setup() {
        every { savedStateHandle["tipo"] } returns "entrada"
        coEvery { getProductos() } returns flowOf(sampleProductos)
        viewModel = MovimientoFormViewModel(savedStateHandle, getProductos, registrarMovimiento)
    }

    @Test
    fun `initial state is entrada from savedStateHandle`() = runTest {
        assertThat(viewModel.uiState.value.tipo).isEqualTo(TipoMovimiento.entrada)
    }

    @Test
    fun `initial state loads products`() = runTest {
        assertThat(viewModel.uiState.value.productos).isEqualTo(sampleProductos)
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `tipo change updates state`() = runTest {
        viewModel.onTipoChanged(TipoMovimiento.salida)

        assertThat(viewModel.uiState.value.tipo).isEqualTo(TipoMovimiento.salida)
    }

    @Test
    fun `producto selected updates state`() = runTest {
        viewModel.onProductoSelected(sampleProductos[0])

        assertThat(viewModel.uiState.value.selectedProducto).isEqualTo(sampleProductos[0])
    }

    @Test
    fun `cantidad changed updates state`() = runTest {
        viewModel.onCantidadChanged("25")

        assertThat(viewModel.uiState.value.cantidad).isEqualTo("25")
    }

    @Test
    fun `notas changed updates state`() = runTest {
        viewModel.onNotasChanged("Compra proveedor")

        assertThat(viewModel.uiState.value.notas).isEqualTo("Compra proveedor")
    }

    @Test
    fun `onRegistrar shows error when no producto selected`() = runTest {
        viewModel.onCantidadChanged("50")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.fieldErrors).containsKey("producto")
        assertThat(viewModel.uiState.value.fieldErrors["producto"]).isEqualTo("Seleccione un producto")
    }

    @Test
    fun `onRegistrar shows error when cantidad is blank`() = runTest {
        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.fieldErrors).containsKey("cantidad")
        assertThat(viewModel.uiState.value.fieldErrors["cantidad"]).isEqualTo("Debe ser un número mayor a 0")
    }

    @Test
    fun `onRegistrar shows error when cantidad is zero`() = runTest {
        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("0")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.fieldErrors).containsKey("cantidad")
        assertThat(viewModel.uiState.value.fieldErrors["cantidad"]).isEqualTo("Debe ser un número mayor a 0")
    }

    @Test
    fun `onRegistrar shows error when cantidad is not a number`() = runTest {
        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("abc")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.fieldErrors).containsKey("cantidad")
    }

    @Test
    fun `onRegistrar shows error for salida when stock insufficient`() = runTest {
        coEvery {
            registrarMovimiento(2, TipoMovimiento.salida, 10, null)
        } returns Result.failure(
            com.inventario.app.domain.error.DomainError.Validation("cantidad", "Stock insuficiente (3 disponible)")
        )

        viewModel.onTipoChanged(TipoMovimiento.salida)
        viewModel.onProductoSelected(sampleProductos[1])
        viewModel.onCantidadChanged("10")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.error).contains("Stock insuficiente")
    }

    @Test
    fun `onRegistrar calls use case on valid input`() = runTest {
        coEvery {
            registrarMovimiento(1, TipoMovimiento.entrada, 50, "Compra proveedor")
        } returns Result.success(sampleMovimiento)

        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("50")
        viewModel.onNotasChanged("Compra proveedor")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onRegistrar emits success on valid registration`() = runTest {
        coEvery {
            registrarMovimiento(1, TipoMovimiento.entrada, 50, null)
        } returns Result.success(sampleMovimiento)

        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("50")
        viewModel.onRegistrar()

        viewModel.navigationEvent.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `onRegistrar shows error on failure`() = runTest {
        coEvery {
            registrarMovimiento(1, TipoMovimiento.entrada, 50, null)
        } returns Result.failure(RuntimeException("Error al registrar"))

        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("50")
        viewModel.onRegistrar()

        assertThat(viewModel.uiState.value.error).isEqualTo("Error al registrar")
    }

    @Test
    fun `onDismissError clears the error`() = runTest {
        coEvery {
            registrarMovimiento(1, TipoMovimiento.entrada, 50, null)
        } returns Result.failure(RuntimeException("Error"))

        viewModel.onProductoSelected(sampleProductos[0])
        viewModel.onCantidadChanged("50")
        viewModel.onRegistrar()
        viewModel.onDismissError()

        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `default tipo is entrada when no savedStateHandle`() = runTest {
        val emptySavedState = mockk<SavedStateHandle>()
        every { emptySavedState["tipo"] } returns null
        coEvery { getProductos() } returns flowOf(emptyList())

        val vm = MovimientoFormViewModel(emptySavedState, getProductos, registrarMovimiento)

        assertThat(vm.uiState.value.tipo).isEqualTo(TipoMovimiento.entrada)
    }
}
