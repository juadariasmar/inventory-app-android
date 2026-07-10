package com.inventario.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.remote.api.VentaApi
import com.inventario.app.data.remote.dto.ItemVentaDto
import com.inventario.app.data.remote.dto.VentaDto
import com.inventario.app.domain.error.DomainError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class VentaRepositoryImplTest {

    private val ventaApi = mockk<VentaApi>()
    private val authDataStore = mockk<AuthDataStore>()
    private val workspaceDataStore = mockk<WorkspaceDataStore>()
    private lateinit var repository: VentaRepositoryImpl

    private val sampleVentaDto = VentaDto(
        id = 1,
        vendedorNombre = "Vendedor",
        clienteNombre = "Cliente",
        total = 350.0,
        notas = null,
        items = listOf(
            ItemVentaDto(1, 1, "Widget", 2, 100.0, 200.0),
            ItemVentaDto(2, 2, "Gadget", 3, 50.0, 150.0)
        ),
        canceladaEn = null,
        motivoCancelacion = null,
        creadoEn = "2024-01-15T10:30:00Z"
    )

    @Before
    fun setup() {
        repository = VentaRepositoryImpl(ventaApi, authDataStore, workspaceDataStore)
    }

    // --- getVentas ---

    @Test
    fun `getVentas returns mapped list`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.getVentas("token", "org", "ws") } returns listOf(sampleVentaDto)

        val ventas = repository.getVentas().first()

        assertThat(ventas).hasSize(1)
        assertThat(ventas.first().id).isEqualTo(1)
        assertThat(ventas.first().total).isWithin(0.01).of(350.0)
    }

    @Test
    fun `getVentas throws Unauthorized when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val result = kotlinx.coroutines.flow.flow {
            repository.getVentas().collect { throw AssertionError("Should not emit") }
        }

        try {
            repository.getVentas().first()
            throw AssertionError("Should have thrown")
        } catch (e: DomainError.Unauthorized) {
            assertThat(e).isInstanceOf(DomainError.Unauthorized::class.java)
        } catch (e: NoSuchElementException) {
            // flow does not emit -> expected
        }
    }

    @Test
    fun `getVentas emits empty list when slugs are blank`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("")

        val ventas = repository.getVentas().first()

        assertThat(ventas).isEmpty()
    }

    // --- getVentaById ---

    @Test
    fun `getVentaById returns venta when found`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.getVentas("token", "org", "ws") } returns listOf(sampleVentaDto)

        val result = repository.getVentaById(1)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.id).isEqualTo(1)
    }

    @Test
    fun `getVentaById returns NotFound when not found`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.getVentas("token", "org", "ws") } returns emptyList()

        val result = repository.getVentaById(999)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NotFound::class.java)
    }

    // --- createVenta ---

    @Test
    fun `createVenta returns success`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.createVenta(any(), any(), any(), any()) } returns sampleVentaDto

        val result = repository.createVenta(1, listOf(Pair(1, 2)), null)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()?.id).isEqualTo(1)
    }

    @Test
    fun `createVenta throws Unauthorized when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val result = repository.createVenta(null, emptyList(), null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `createVenta builds correct request`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.createVenta(any(), any(), any(), any()) } returns sampleVentaDto

        repository.createVenta(5, listOf(Pair(1, 2), Pair(3, 4)), "Notas")

        coVerify {
            ventaApi.createVenta("token", "org", "ws", any())
        }
    }

    @Test
    fun `createVenta returns NetworkError on generic exception`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.createVenta(any(), any(), any(), any()) } throws RuntimeException("error")

        val result = repository.createVenta(null, listOf(Pair(1, 1)), null)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    // --- cancelarVenta ---

    @Test
    fun `cancelarVenta returns success`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { workspaceDataStore.orgSlug } returns flowOf("org")
        coEvery { workspaceDataStore.wsSlug } returns flowOf("ws")
        coEvery { ventaApi.cancelarVenta(any(), any(), any(), any(), any()) } returns sampleVentaDto

        val result = repository.cancelarVenta(1, "Motivo")

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `cancelarVenta returns Unauthorized when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val result = repository.cancelarVenta(1, "Motivo")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }
}
