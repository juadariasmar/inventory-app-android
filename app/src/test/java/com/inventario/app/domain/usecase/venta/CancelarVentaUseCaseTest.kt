package com.inventario.app.domain.usecase.venta

import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.repository.VentaRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CancelarVentaUseCaseTest {

    private val repository = mockk<VentaRepository>()
    private lateinit var useCase: CancelarVentaUseCase

    @Before
    fun setup() {
        useCase = CancelarVentaUseCase(repository)
    }

    @Test
    fun `invoke fails with blank motivo`() = runTest {
        val result = useCase(1, "")
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke fails with whitespace motivo`() = runTest {
        val result = useCase(1, "   ")
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke succeeds with valid motivo`() = runTest {
        coEvery { repository.cancelarVenta(any(), any()) } returns Result.success(Unit)
        val result = useCase(1, "Cliente canceló")
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke calls repository with correct parameters`() = runTest {
        coEvery { repository.cancelarVenta(any(), any()) } returns Result.success(Unit)
        useCase(42, "Producto dañado")
        coVerify { repository.cancelarVenta(42, "Producto dañado") }
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        coEvery { repository.cancelarVenta(any(), any()) } returns Result.failure(Exception("Error"))
        val result = useCase(1, "Motivo")
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke succeeds with short motivo`() = runTest {
        coEvery { repository.cancelarVenta(any(), any()) } returns Result.success(Unit)
        val result = useCase(1, "No")
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke succeeds with long motivo`() = runTest {
        val longMotivo = "El cliente solicitó la cancelación porque el producto no cumple con las especificaciones acordadas"
        coEvery { repository.cancelarVenta(any(), any()) } returns Result.success(Unit)
        val result = useCase(1, longMotivo)
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke fails for zero id`() = runTest {
        val result = useCase(0, "")
        assertThat(result.isFailure).isTrue()
    }
}
