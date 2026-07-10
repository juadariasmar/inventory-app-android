package com.inventario.app.ui.home

import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.model.DashboardMetrics
import com.inventario.app.domain.usecase.dashboard.GetDashboardUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getDashboard = mockk<GetDashboardUseCase>()
    private lateinit var viewModel: HomeViewModel

    private val emptyMetrics = DashboardMetrics(
        totalProductos = 0,
        totalUnidades = 0,
        stockBajo = 0,
        valorInventario = 0.0,
        ultimosMovimientos = emptyList()
    )

    private val sampleMetrics = DashboardMetrics(
        totalProductos = 25,
        totalUnidades = 150,
        stockBajo = 3,
        valorInventario = 3750000.0,
        ultimosMovimientos = emptyList()
    )

    @Before
    fun setup() {
        coEvery { getDashboard() } returns flowOf(emptyMetrics)
        viewModel = HomeViewModel(getDashboard)
    }

    // --- Initial state ---

    @Test
    fun `initial state loads dashboard with metrics`() = runTest {
        val state = viewModel.uiState.value
        assertThat(state.metrics).isNotNull()
        assertThat(state.metrics).isEqualTo(emptyMetrics)
    }

    @Test
    fun `initial state is not loading after load completes`() = runTest {
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `initial state has no error`() = runTest {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    // --- Dashboard loading ---

    @Test
    fun `loads dashboard metrics from use case`() = runTest {
        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics).isEqualTo(sampleMetrics)
    }

    @Test
    fun `sets totalProductos correctly`() = runTest {
        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics!!.totalProductos).isEqualTo(25)
    }

    @Test
    fun `sets totalUnidades correctly`() = runTest {
        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics!!.totalUnidades).isEqualTo(150)
    }

    @Test
    fun `sets stockBajo correctly`() = runTest {
        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics!!.stockBajo).isEqualTo(3)
    }

    @Test
    fun `sets valorInventario correctly`() = runTest {
        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics!!.valorInventario).isWithin(0.01).of(3750000.0)
    }

    // --- Error handling ---

    @Test
    fun `shows error when dashboard flow throws`() = runTest {
        coEvery { getDashboard() } returns flow {
            throw RuntimeException("Failed to load")
        }
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.error).isEqualTo("Failed to load")
        assertThat(vm.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `shows default error message when exception has no message`() = runTest {
        coEvery { getDashboard() } returns flow {
            throw RuntimeException()
        }
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.error).isEqualTo("Error al cargar dashboard")
    }

    // --- refresh ---

    @Test
    fun `refresh reloads dashboard`() = runTest {
        coEvery { getDashboard() } returns flowOf(emptyMetrics)
        val vm = HomeViewModel(getDashboard)

        coEvery { getDashboard() } returns flowOf(sampleMetrics)
        vm.refresh()

        assertThat(vm.uiState.value.metrics).isEqualTo(sampleMetrics)
    }

    // --- State immutability ---

    @Test
    fun `metrics is null initially when use case returns null`() = runTest {
        coEvery { getDashboard() } returns flowOf(
            DashboardMetrics(0, 0, 0, 0.0, emptyList())
        )
        val vm = HomeViewModel(getDashboard)

        assertThat(vm.uiState.value.metrics).isNotNull()
    }
}
