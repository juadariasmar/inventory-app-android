package com.inventario.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.remote.api.AuthApi
import com.inventario.app.data.remote.dto.SessionResponse
import com.inventario.app.data.remote.dto.SignInResponse
import com.inventario.app.data.remote.dto.UserDto
import com.inventario.app.domain.error.DomainError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private val authApi = mockk<AuthApi>()
    private val authDataStore = mockk<AuthDataStore>(relaxed = true)
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun setup() {
        coEvery { authDataStore.getToken() } returns null
        repository = AuthRepositoryImpl(authApi, authDataStore)
    }

    // --- login ---

    @Test
    fun `login returns success on valid credentials`() = runTest {
        val signInResponse = SignInResponse(
            redirect = false,
            token = "test-token-123",
            user = UserDto(id = "1", email = "test@email.com", name = "Test", rol = "USUARIO")
        )
        coEvery { authApi.signIn(any(), any()) } returns signInResponse
        coEvery { authDataStore.saveSession(any(), any(), any(), any(), any()) } just mockk()

        val result = repository.login("test@email.com", "password")

        assertThat(result.isSuccess).isTrue()
        val sesion = result.getOrNull()!!
        assertThat(sesion.userId).isEqualTo("1")
        assertThat(sesion.email).isEqualTo("test@email.com")
        assertThat(sesion.nombre).isEqualTo("Test")
        assertThat(sesion.rol).isEqualTo("USUARIO")
    }

    @Test
    fun `login saves session to data store`() = runTest {
        val signInResponse = SignInResponse(
            redirect = false,
            token = "test-token-123",
            user = UserDto(id = "1", email = "test@email.com", name = "Test", rol = "ADMIN")
        )
        coEvery { authApi.signIn(any(), any()) } returns signInResponse
        coEvery { authDataStore.saveSession(any(), any(), any(), any(), any()) } just mockk()

        repository.login("test@email.com", "password")

        coVerify {
            authDataStore.saveSession(
                token = "test-token-123",
                userId = "1",
                email = "test@email.com",
                name = "Test",
                rol = "ADMIN"
            )
        }
    }

    @Test
    fun `login returns failure when token is null`() = runTest {
        val signInResponse = SignInResponse(redirect = false, token = null, user = null)
        coEvery { authApi.signIn(any(), any()) } returns signInResponse

        val result = repository.login("test@email.com", "password")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    @Test
    fun `login returns failure when user is null`() = runTest {
        val signInResponse = SignInResponse(redirect = false, token = "token", user = null)
        coEvery { authApi.signIn(any(), any()) } returns signInResponse

        val result = repository.login("test@email.com", "password")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `login maps user name fallback to email when name is null`() = runTest {
        val signInResponse = SignInResponse(
            redirect = false,
            token = "token",
            user = UserDto(id = "1", email = "test@email.com", name = null, rol = "USUARIO")
        )
        coEvery { authApi.signIn(any(), any()) } returns signInResponse
        coEvery { authDataStore.saveSession(any(), any(), any(), any(), any()) } just mockk()

        val result = repository.login("test@email.com", "password")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.nombre).isEqualTo("test@email.com")
    }

    @Test
    fun `login maps rol fallback to USUARIO when rol is null`() = runTest {
        val signInResponse = SignInResponse(
            redirect = false,
            token = "token",
            user = UserDto(id = "1", email = "test@email.com", name = "Test", rol = null)
        )
        coEvery { authApi.signIn(any(), any()) } returns signInResponse
        coEvery { authDataStore.saveSession(any(), any(), any(), any(), any()) } just mockk()

        val result = repository.login("test@email.com", "password")

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()!!.rol).isEqualTo("USUARIO")
    }

    @Test
    fun `login returns NetworkError on generic exception`() = runTest {
        coEvery { authApi.signIn(any(), any()) } throws RuntimeException("connection failed")

        val result = repository.login("test@email.com", "password")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    @Test
    fun `login does not save session when API fails`() = runTest {
        coEvery { authApi.signIn(any(), any()) } throws RuntimeException("error")

        repository.login("test@email.com", "password")

        coVerify(exactly = 0) { authDataStore.saveSession(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `login with redirect response returns NetworkError`() = runTest {
        val signInResponse = SignInResponse(redirect = true, token = null, user = null)
        coEvery { authApi.signIn(any(), any()) } returns signInResponse

        val result = repository.login("test@email.com", "password")

        assertThat(result.isFailure).isTrue()
    }

    // --- getSession ---

    @Test
    fun `getSession returns success with valid token`() = runTest {
        coEvery { authDataStore.token } returns flowOf("valid-token")
        coEvery { authApi.getSession() } returns SessionResponse(
            user = UserDto(id = "1", email = "a@b.com", name = "User", rol = "ADMIN")
        )

        val result = repository.getSession()

        assertThat(result.isSuccess).isTrue()
        val sesion = result.getOrNull()!!
        assertThat(sesion.userId).isEqualTo("1")
        assertThat(sesion.rol).isEqualTo("ADMIN")
    }

    @Test
    fun `getSession returns Unauthorized when user is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.getSession() } returns SessionResponse(user = null)

        val result = repository.getSession()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.Unauthorized::class.java)
    }

    @Test
    fun `getSession sets estado to ACTIVO`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.getSession() } returns SessionResponse(
            user = UserDto(id = "1", email = "a@b.com", name = "User", rol = "USUARIO")
        )

        val result = repository.getSession()

        assertThat(result.getOrNull()!!.estado).isEqualTo("ACTIVO")
    }

    @Test
    fun `getSession maps name fallback to email`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.getSession() } returns SessionResponse(
            user = UserDto(id = "1", email = "a@b.com", name = null, rol = "USUARIO")
        )

        val result = repository.getSession()

        assertThat(result.getOrNull()!!.nombre).isEqualTo("a@b.com")
    }

    @Test
    fun `getSession returns NetworkError on generic exception`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.getSession() } throws RuntimeException("error")

        val result = repository.getSession()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isInstanceOf(DomainError.NetworkError::class.java)
    }

    // --- logout ---

    @Test
    fun `logout clears session from data store`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.signOut() } just mockk()
        coEvery { authDataStore.clearSession() } just mockk()

        repository.logout()

        coVerify { authDataStore.clearSession() }
    }

    @Test
    fun `logout calls signOut with token`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token-123")
        coEvery { authApi.signOut() } just mockk()
        coEvery { authDataStore.clearSession() } just mockk()

        repository.logout()

        coVerify { authApi.signOut() }
    }

    @Test
    fun `logout clears session even when API call fails`() = runTest {
        coEvery { authDataStore.token } returns flowOf("token")
        coEvery { authApi.signOut() } throws RuntimeException("network error")
        coEvery { authDataStore.clearSession() } just mockk()

        repository.logout()

        coVerify { authDataStore.clearSession() }
    }

    @Test
    fun `logout clears session when token is null`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)
        coEvery { authDataStore.clearSession() } just mockk()

        repository.logout()

        coVerify { authDataStore.clearSession() }
    }

    // --- observeToken ---

    @Test
    fun `observeToken returns token from data store`() = runTest {
        coEvery { authDataStore.token } returns flowOf("my-token")

        val token = repository.observeToken().first()

        assertThat(token).isEqualTo("my-token")
    }

    @Test
    fun `observeToken returns null when no token`() = runTest {
        coEvery { authDataStore.token } returns flowOf(null)

        val token = repository.observeToken().first()

        assertThat(token).isNull()
    }
}
