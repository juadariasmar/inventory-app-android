package com.inventario.app.domain.usecase.auth

import com.google.common.truth.Truth.assertThat
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetSessionUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private lateinit var useCase: GetSessionUseCase

    private val dummySesion = Sesion(
        userId = "1",
        email = "test@email.com",
        nombre = "Test User",
        rol = "ADMIN",
        estado = "ACTIVO",
        permisos = listOf("REGISTRAR_MOVIMIENTOS")
    )

    @Before
    fun setup() {
        useCase = GetSessionUseCase(authRepository)
    }

    @Test
    fun `invoke returns session from repository`() = runTest {
        coEvery { authRepository.getSession() } returns Result.success(dummySesion)

        val result = useCase()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(dummySesion)
    }

    @Test
    fun `invoke returns failure when session not found`() = runTest {
        coEvery { authRepository.getSession() } returns Result.failure(DomainError.Unauthorized)

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `invoke returns failure when network error`() = runTest {
        coEvery { authRepository.getSession() } returns Result.failure(DomainError.NetworkError)

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    @Test
    fun `invoke calls repository exactly once`() = runTest {
        coEvery { authRepository.getSession() } returns Result.success(dummySesion)

        useCase()

        coVerify(exactly = 1) { authRepository.getSession() }
    }

    @Test
    fun `invoke returns session with correct user data`() = runTest {
        coEvery { authRepository.getSession() } returns Result.success(dummySesion)

        val result = useCase()
        val sesion = result.getOrNull()!!

        assertThat(sesion.userId).isEqualTo("1")
        assertThat(sesion.email).isEqualTo("test@email.com")
        assertThat(sesion.nombre).isEqualTo("Test User")
        assertThat(sesion.rol).isEqualTo("ADMIN")
    }

    @Test
    fun `invoke propagates Forbidden error`() = runTest {
        coEvery { authRepository.getSession() } returns Result.failure(DomainError.Forbidden)

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Forbidden::class.java)
    }

    @Test
    fun `invoke propagates ServerError`() = runTest {
        coEvery { authRepository.getSession() } returns Result.failure(DomainError.ServerError(503))

        val result = useCase()

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.ServerError
        assertThat(error.code).isEqualTo(503)
    }
}
