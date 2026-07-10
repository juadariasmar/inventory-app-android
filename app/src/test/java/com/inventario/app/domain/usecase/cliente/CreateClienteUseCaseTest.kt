package com.inventario.app.domain.usecase.cliente

import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.model.Cliente
import com.inventario.app.domain.repository.ClienteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateClienteUseCaseTest {

    private val repository = mockk<ClienteRepository>()
    private lateinit var useCase: CreateClienteUseCase

    @Before
    fun setup() {
        useCase = CreateClienteUseCase(repository)
    }

    @Test
    fun `invoke fails with blank nombre`() = runTest {
        val result = useCase(Cliente(0, "", null, null, null, null, null))
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke fails with whitespace nombre`() = runTest {
        val result = useCase(Cliente(0, "   ", null, null, null, null, null))
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke succeeds with valid nombre`() = runTest {
        coEvery { repository.createCliente(any()) } returns Result.success(mockk())
        val result = useCase(Cliente(0, "Juan", null, null, null, null, null))
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke calls repository with complete cliente`() = runTest {
        val cliente = Cliente(0, "Juan Pérez", "12345678", "juan@test.com", "555-0100", "Calle 123", "Cliente VIP")
        coEvery { repository.createCliente(any()) } returns Result.success(cliente)
        useCase(cliente)
        coVerify { repository.createCliente(cliente) }
    }

    @Test
    fun `invoke passes cliente with only nombre`() = runTest {
        val cliente = Cliente(0, "Juan", null, null, null, null, null)
        coEvery { repository.createCliente(any()) } returns Result.success(cliente)
        useCase(cliente)
        coVerify { repository.createCliente(cliente) }
    }

    @Test
    fun `invoke propagates repository errors`() = runTest {
        coEvery { repository.createCliente(any()) } returns Result.failure(Exception("Error"))
        val result = useCase(Cliente(0, "Juan", null, null, null, null, null))
        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `invoke returns created cliente with id`() = runTest {
        val created = Cliente(42, "Juan", null, null, null, null, null)
        coEvery { repository.createCliente(any()) } returns Result.success(created)
        val result = useCase(Cliente(0, "Juan", null, null, null, null, null))
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.id).isEqualTo(42)
    }

    @Test
    fun `invoke succeeds with all optional fields`() = runTest {
        val cliente = Cliente(
            id = 0,
            nombre = "María García",
            documento = "87654321",
            email = "maria@test.com",
            telefono = "555-0200",
            direccion = "Av. Principal 456",
            notas = "Contactar después de 5pm"
        )
        coEvery { repository.createCliente(any()) } returns Result.success(cliente)
        val result = useCase(cliente)
        assertThat(result.isSuccess).isTrue()
    }
}
