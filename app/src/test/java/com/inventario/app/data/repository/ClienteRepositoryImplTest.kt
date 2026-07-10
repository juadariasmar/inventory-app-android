package com.inventario.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.remote.api.ClienteApi
import com.inventario.app.data.remote.dto.ClienteDto
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Cliente
import io.ktor.client.plugins.ResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ClienteRepositoryImplTest {

    private val clienteApi = mockk<ClienteApi>()
    private val authDataStore = mockk<AuthDataStore>()
    private val workspaceDataStore = mockk<WorkspaceDataStore>()
    private lateinit var repository: ClienteRepositoryImpl

    private val sampleClienteDto = ClienteDto(
        id = 1,
        nombre = "Juan Pérez",
        documento = "12345678",
        email = "juan@test.com",
        telefono = "555-0100",
        direccion = "Calle 123",
        notas = "Cliente VIP"
    )

    private val sampleCliente = Cliente(
        id = 1,
        nombre = "Juan Pérez",
        documento = "12345678",
        email = "juan@test.com",
        telefono = "555-0100",
        direccion = "Calle 123",
        notas = "Cliente VIP"
    )

    @Before
    fun setup() {
        repository = ClienteRepositoryImpl(clienteApi, authDataStore, workspaceDataStore)
    }

    // --- getClientes ---

    @Test
    fun `getClientes returns mapped list`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.getClientes("token", "org", "ws") } returns listOf(sampleClienteDto)

        val clientes = repository.getClientes().first()

        assertThat(clientes).hasSize(1)
        assertThat(clientes.first().nombre).isEqualTo("Juan Pérez")
        assertThat(clientes.first().email).isEqualTo("juan@test.com")
    }

    @Test
    fun `getClientes emits empty list when slugs are blank`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("")

        val clientes = repository.getClientes().first()

        assertThat(clientes).isEmpty()
    }

    // --- getClienteById ---

    @Test
    fun `getClienteById returns success`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.getClienteById("token", "org", "ws", 1) } returns sampleClienteDto

        val result = repository.getClienteById(1)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.nombre).isEqualTo("Juan Pérez")
    }

    @Test
    fun `getClienteById returns Unauthorized when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val result = repository.getClienteById(1)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    // --- createCliente ---

    @Test
    fun `createCliente returns success`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.createCliente(any(), any(), any(), any()) } returns sampleClienteDto

        val result = repository.createCliente(sampleCliente)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.id).isEqualTo(1)
    }

    @Test
    fun `createCliente builds correct request`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.createCliente(any(), any(), any(), any()) } returns sampleClienteDto

        repository.createCliente(sampleCliente)

        coVerify {
            clienteApi.createCliente("token", "org", "ws", any())
        }
    }

    @Test
    fun `createCliente returns NetworkError on exception`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.createCliente(any(), any(), any(), any()) } throws RuntimeException("error")

        val result = repository.createCliente(sampleCliente)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    // --- updateCliente ---

    @Test
    fun `updateCliente returns success`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { clienteApi.updateCliente(any(), any(), any(), any(), any()) } returns sampleClienteDto

        val result = repository.updateCliente(1, sampleCliente)

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `updateCliente returns Unauthorized when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val result = repository.updateCliente(1, sampleCliente)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `updateCliente returns NotFound when API returns 404`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")

        val mockResponse = mockk<HttpResponse>(relaxed = true) {
            every { status } returns HttpStatusCode(404, "Not Found")
        }
        val responseException = ResponseException(mockResponse, "Not Found")
        coEvery { clienteApi.updateCliente(any(), any(), any(), any(), any()) } throws responseException

        val result = repository.updateCliente(999, sampleCliente)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NotFound::class.java)
    }
}
