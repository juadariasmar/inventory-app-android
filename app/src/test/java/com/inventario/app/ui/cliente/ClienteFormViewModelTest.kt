package com.inventario.app.ui.cliente

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.usecase.cliente.CreateClienteUseCase
import com.inventario.app.domain.usecase.cliente.GetClienteByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ClienteFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val createCliente = mockk<CreateClienteUseCase>()
    private val getClienteById = mockk<GetClienteByIdUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    private lateinit var viewModel: ClienteFormViewModel

    @Before
    fun setup() {
        every { savedStateHandle["clienteId"] } returns null
        viewModel = ClienteFormViewModel(savedStateHandle, createCliente, getClienteById)
    }

    @Test
    fun `initial state has empty fields`() = runTest {
        assertThat(viewModel.uiState.value.nombre).isEmpty()
        assertThat(viewModel.uiState.value.documento).isEmpty()
        assertThat(viewModel.uiState.value.email).isEmpty()
        assertThat(viewModel.uiState.value.telefono).isEmpty()
        assertThat(viewModel.uiState.value.direccion).isEmpty()
        assertThat(viewModel.uiState.value.notas).isEmpty()
    }

    @Test
    fun `initial state is not editing`() = runTest {
        assertThat(viewModel.isEditing).isFalse()
    }

    @Test
    fun `isEditing is true when clienteId is present`() = runTest {
        val editHandle = mockk<SavedStateHandle>()
        every { editHandle["clienteId"] } returns 5
        val editVm = ClienteFormViewModel(editHandle, createCliente, getClienteById)
        assertThat(editVm.isEditing).isTrue()
    }

    @Test
    fun `onNombreChanged updates nombre`() = runTest {
        viewModel.onNombreChanged("Juan Pérez")
        assertThat(viewModel.uiState.value.nombre).isEqualTo("Juan Pérez")
    }

    @Test
    fun `onDocumentoChanged updates documento`() = runTest {
        viewModel.onDocumentoChanged("12345678")
        assertThat(viewModel.uiState.value.documento).isEqualTo("12345678")
    }

    @Test
    fun `onEmailChanged updates email`() = runTest {
        viewModel.onEmailChanged("juan@test.com")
        assertThat(viewModel.uiState.value.email).isEqualTo("juan@test.com")
    }

    @Test
    fun `onTelefonoChanged updates telefono`() = runTest {
        viewModel.onTelefonoChanged("555-0100")
        assertThat(viewModel.uiState.value.telefono).isEqualTo("555-0100")
    }

    @Test
    fun `onDireccionChanged updates direccion`() = runTest {
        viewModel.onDireccionChanged("Calle 123")
        assertThat(viewModel.uiState.value.direccion).isEqualTo("Calle 123")
    }

    @Test
    fun `onNotasChanged updates notas`() = runTest {
        viewModel.onNotasChanged("Cliente VIP")
        assertThat(viewModel.uiState.value.notas).isEqualTo("Cliente VIP")
    }

    @Test
    fun `onGuardar fails with blank nombre`() = runTest {
        viewModel.onGuardar()
        assertThat(viewModel.uiState.value.fieldErrors).containsKey("nombre")
        assertThat(viewModel.uiState.value.fieldErrors["nombre"]).isEqualTo("Nombre es requerido")
    }

    @Test
    fun `onGuardar fails with whitespace nombre`() = runTest {
        viewModel.onNombreChanged("   ")
        viewModel.onGuardar()
        assertThat(viewModel.uiState.value.fieldErrors).containsKey("nombre")
    }

    @Test
    fun `onGuardar emits navigation event on success`() = runTest {
        coEvery { createCliente(any()) } returns Result.success(Cliente(1, "Juan", null, null, null, null, null))
        viewModel.onNombreChanged("Juan")
        viewModel.onGuardar()

        viewModel.navigationEvent.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `onGuardar shows error on failure`() = runTest {
        coEvery { createCliente(any()) } returns Result.failure(RuntimeException("Error al crear cliente"))
        viewModel.onNombreChanged("Juan")
        viewModel.onGuardar()

        assertThat(viewModel.uiState.value.error).isEqualTo("Error al crear cliente")
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onDismissError clears error`() = runTest {
        coEvery { createCliente(any()) } returns Result.failure(RuntimeException("Error"))
        viewModel.onNombreChanged("Juan")
        viewModel.onGuardar()
        viewModel.onDismissError()

        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `onGuardar sends null for blank optional fields`() = runTest {
        coEvery { createCliente(any()) } returns Result.success(mockk())
        viewModel.onNombreChanged("Juan")
        viewModel.onGuardar()

        coVerify {
            createCliente(
                Cliente(0, "Juan", null, null, null, null, null)
            )
        }
    }

    @Test
    fun `onGuardar sends all optional fields`() = runTest {
        coEvery { createCliente(any()) } returns Result.success(mockk())
        viewModel.onNombreChanged("María García")
        viewModel.onDocumentoChanged("87654321")
        viewModel.onEmailChanged("maria@test.com")
        viewModel.onTelefonoChanged("555-0200")
        viewModel.onDireccionChanged("Av. Principal")
        viewModel.onNotasChanged("Nota importante")
        viewModel.onGuardar()

        coVerify {
            createCliente(
                Cliente(0, "María García", "87654321", "maria@test.com", "555-0200", "Av. Principal", "Nota importante")
            )
        }
    }
}
