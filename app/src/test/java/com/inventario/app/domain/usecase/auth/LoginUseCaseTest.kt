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

class LoginUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private lateinit var useCase: LoginUseCase

    private val dummySesion = Sesion(
        userId = "1",
        email = "test@email.com",
        nombre = "Test User",
        rol = "USUARIO",
        estado = "ACTIVO",
        permisos = emptyList()
    )

    @Before
    fun setup() {
        useCase = LoginUseCase(authRepository)
    }

    @Test
    fun `invoke returns failure for blank email`() = runTest {
        val result = useCase("", "password123")

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.Validation
        assertThat(error.field).isEqualTo("email")
    }

    @Test
    fun `invoke returns failure for whitespace-only email`() = runTest {
        val result = useCase("   ", "password123")

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.Validation
        assertThat(error.field).isEqualTo("email")
    }

    @Test
    fun `invoke returns failure for blank password`() = runTest {
        val result = useCase("test@email.com", "")

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.Validation
        assertThat(error.field).isEqualTo("password")
    }

    @Test
    fun `invoke returns failure for whitespace-only password`() = runTest {
        val result = useCase("test@email.com", "   ")

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.Validation
        assertThat(error.field).isEqualTo("password")
    }

    @Test
    fun `invoke does not call repository when email is blank`() = runTest {
        useCase("", "password123")

        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `invoke does not call repository when password is blank`() = runTest {
        useCase("test@email.com", "")

        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `invoke calls repository on valid input`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(dummySesion)

        useCase("test@email.com", "password123")

        coVerify(exactly = 1) { authRepository.login("test@email.com", "password123") }
    }

    @Test
    fun `invoke returns success session from repository`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(dummySesion)

        val result = useCase("test@email.com", "password123")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(dummySesion)
    }

    @Test
    fun `invoke returns failure when repository returns NetworkError`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(DomainError.NetworkError)

        val result = useCase("test@email.com", "password123")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    @Test
    fun `invoke returns failure when repository returns Unauthorized`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(DomainError.Unauthorized)

        val result = useCase("test@email.com", "password123")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `invoke returns failure when repository returns ServerError`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.failure(DomainError.ServerError(500))

        val result = useCase("test@email.com", "password123")

        assertThat(result.isFailure).isTrue()
        val error = result.exceptionOrNull() as DomainError.ServerError
        assertThat(error.code).isEqualTo(500)
    }

    @Test
    fun `invoke passes trimmed email to repository`() = runTest {
        coEvery { authRepository.login(any(), any()) } returns Result.success(dummySesion)

        useCase("  test@email.com  ", "password123")

        coVerify { authRepository.login("test@email.com", "password123") }
    }
}
