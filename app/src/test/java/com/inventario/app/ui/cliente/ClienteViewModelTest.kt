package com.inventario.app.ui.cliente

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.usecase.cliente.GetClientesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ClienteViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getClientes = mockk<GetClientesUseCase>()
    private lateinit var viewModel: ClienteViewModel

    private val sampleClientes = listOf(
        Cliente(1, "Juan Pérez", "12345678", "juan@test.com", "555-0100", "Calle 123", "VIP"),
        Cliente(2, "María García", "87654321", "maria@test.com", "555-0200", "Av. Principal", null),
        Cliente(3, "Carlos López", null, null, null, null, null)
    )

    @Before
    fun setup() {
        coEvery { getClientes() } returns flowOf(sampleClientes)
        viewModel = ClienteViewModel(getClientes)
    }

    @Test
    fun `initial state loads clients`() = runTest {
        assertThat(viewModel.uiState.value.clientes).hasSize(3)
        assertThat(viewModel.uiState.value.isLoading).isFalse()
        assertThat(viewModel.uiState.value.searchQuery).isEmpty()
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `search filters client list by nombre`() = runTest {
        viewModel.onSearchQueryChanged("Juan")

        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("Juan")
    }

    @Test
    fun `search filters client list by documento`() = runTest {
        viewModel.onSearchQueryChanged("87654321")

        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("87654321")
    }

    @Test
    fun `search with empty query shows all clients`() = runTest {
        viewModel.onSearchQueryChanged("Juan")
        viewModel.onSearchQueryChanged("")

        assertThat(viewModel.uiState.value.searchQuery).isEmpty()
    }

    @Test
    fun `search is case insensitive`() = runTest {
        viewModel.onSearchQueryChanged("juan pérez")

        assertThat(viewModel.uiState.value.searchQuery).isEqualTo("juan pérez")
    }

    @Test
    fun `error handling shows error message`() = runTest {
        coEvery { getClientes() } returns flow {
            throw RuntimeException("Failed to load")
        }
        val vm = ClienteViewModel(getClientes)

        assertThat(vm.uiState.value.error).isEqualTo("Failed to load")
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onDismissError clears error`() = runTest {
        coEvery { getClientes() } returns flow {
            throw RuntimeException("Error")
        }
        val vm = ClienteViewModel(getClientes)

        vm.onDismissError()

        assertThat(vm.uiState.value.error).isNull()
    }

    @Test
    fun `loads empty client list`() = runTest {
        coEvery { getClientes() } returns flowOf(emptyList())
        val vm = ClienteViewModel(getClientes)

        assertThat(vm.uiState.value.clientes).isEmpty()
        assertThat(vm.uiState.value.isLoading).isFalse()
    }
}
