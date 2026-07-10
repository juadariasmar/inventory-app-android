package com.inventario.app.domain.usecase.venta

import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.model.ItemVenta
import com.inventario.app.domain.model.Venta
import com.inventario.app.domain.repository.VentaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateVentaUseCaseTest {

    private val repository = mockk<VentaRepository>()
    private lateinit var useCase: CreateVentaUseCase

    private val sampleVenta = Venta(
        id = 1,
        vendedorNombre = "Vendedor",
        clienteNombre = "Cliente",
        total = 350.0,
        notas = null,
        items = listOf(
            ItemVenta(1, 1, "Widget", 2, 100.0, 200.0),
            ItemVenta(2, 2, "Gadget", 3, 50.0, 150.0)
        ),
        canceladaEn = null,
        motivoCancelacion = null,
        creadoEn = "2024-01-15T10:30:00Z"
    )

    @Before
    fun setup() {
        useCase = CreateVentaUseCase(repository)
    }

    @Test
    fun `invoke fails with empty items`() = runTest {
        val result = useCase(null, emptyList(), null)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke fails when items list is null`() = runTest {
        val result = useCase(null, emptyList(), null)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke succeeds with valid data`() = runTest {
        coEvery { repository.createVenta(any(), any(), any()) } returns Result.success(sampleVenta)
        val result = useCase(1, listOf(Pair(1, 2)), null)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke fails with zero quantity item`() = runTest {
        val result = useCase(null, listOf(Pair(1, 0)), null)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke fails with negative quantity item`() = runTest {
        val result = useCase(null, listOf(Pair(1, -1)), null)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke passes clienteId and notas to repository`() = runTest {
        coEvery { repository.createVenta(any(), any(), any()) } returns Result.success(sampleVenta)
        useCase(5, listOf(Pair(1, 2)), "Nota de prueba")
        coVerify { repository.createVenta(5, listOf(Pair(1, 2)), "Nota de prueba") }
    }

    @Test
    fun `invoke passes null clienteId and notas`() = runTest {
        coEvery { repository.createVenta(any(), any(), any()) } returns Result.success(sampleVenta)
        useCase(null, listOf(Pair(1, 2)), null)
        coVerify { repository.createVenta(null, listOf(Pair(1, 2)), null) }
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        coEvery { repository.createVenta(any(), any(), any()) } returns Result.failure(Exception("Error"))
        val result = useCase(null, listOf(Pair(1, 1)), null)
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke returns success with multiple items`() = runTest {
        coEvery { repository.createVenta(any(), any(), any()) } returns Result.success(sampleVenta)
        val result = useCase(1, listOf(Pair(1, 2), Pair(2, 3)), null)
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.total).isEqualTo(350.0)
    }

    @Test
    fun `invoke validates all items have positive quantity`() = runTest {
        val result = useCase(null, listOf(Pair(1, 2), Pair(2, 0)), null)
        assertThat(result.isFailure).isTrue()
    }
}
