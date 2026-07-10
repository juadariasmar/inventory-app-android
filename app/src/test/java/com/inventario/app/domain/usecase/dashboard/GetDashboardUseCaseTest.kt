package com.inventario.app.domain.usecase.dashboard

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.model.DashboardMetrics
import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import com.inventario.app.domain.repository.DashboardRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetDashboardUseCaseTest {

    private val repository = mockk<DashboardRepository>()
    private lateinit var useCase: GetDashboardUseCase

    private val sampleMetrics = DashboardMetrics(
        totalProductos = 150,
        totalUnidades = 5000,
        stockBajo = 12,
        valorInventario = 12500000.0,
        ultimosMovimientos = listOf(
            Movimiento(
                id = 1,
                productoId = 1,
                productoNombre = "Widget",
                tipo = TipoMovimiento.entrada,
                cantidad = 50,
                notas = null,
                ventaId = null,
                ordenCompraId = null,
                creadoEn = "2024-01-15T10:30:00",
                usuarioNombre = "Admin"
            )
        )
    )

    @Before
    fun setup() {
        useCase = GetDashboardUseCase(repository)
    }

    @Test
    fun `invoke returns metrics from repository`() = runTest {
        coEvery { repository.getDashboardMetrics() } returns flowOf(sampleMetrics)

        useCase().test {
            val metrics = awaitItem()
            assertThat(metrics.totalProductos).isEqualTo(150)
            assertThat(metrics.totalUnidades).isEqualTo(5000)
            assertThat(metrics.stockBajo).isEqualTo(12)
            assertThat(metrics.valorInventario).isWithin(0.01).of(12500000.0)
            assertThat(metrics.ultimosMovimientos).hasSize(1)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty metrics`() = runTest {
        val empty = DashboardMetrics(0, 0, 0, 0.0, emptyList())
        coEvery { repository.getDashboardMetrics() } returns flowOf(empty)

        useCase().test {
            val metrics = awaitItem()
            assertThat(metrics.totalProductos).isEqualTo(0)
            assertThat(metrics.totalUnidades).isEqualTo(0)
            assertThat(metrics.ultimosMovimientos).isEmpty()
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns metrics with zero stock`() = runTest {
        val noStock = sampleMetrics.copy(totalUnidades = 0, stockBajo = 0)
        coEvery { repository.getDashboardMetrics() } returns flowOf(noStock)

        useCase().test {
            val metrics = awaitItem()
            assertThat(metrics.totalUnidades).isEqualTo(0)
            assertThat(metrics.stockBajo).isEqualTo(0)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns metrics with high valorInventario`() = runTest {
        val highValue = sampleMetrics.copy(valorInventario = 99999999.99)
        coEvery { repository.getDashboardMetrics() } returns flowOf(highValue)

        useCase().test {
            val metrics = awaitItem()
            assertThat(metrics.valorInventario).isWithin(0.01).of(99999999.99)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns flow of metrics`() = runTest {
        coEvery { repository.getDashboardMetrics() } returns flowOf(sampleMetrics)

        val flow = useCase()
        flow.test {
            val metrics = awaitItem()
            assertThat(metrics).isNotNull()
            awaitComplete()
        }
    }
}
