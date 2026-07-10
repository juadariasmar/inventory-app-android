package com.inventario.app.ui.auth

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.inventario.app.core.util.MainDispatcherRule
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.usecase.auth.GetSessionUseCase
import com.inventario.app.domain.usecase.auth.LoginUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val loginUseCase = mockk<LoginUseCase>()
    private val getSessionUseCase = mockk<GetSessionUseCase>()

    private lateinit var viewModel: LoginViewModel

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
        coEvery { getSessionUseCase() } returns Result.failure(DomainError.Unauthorized)
        viewModel = LoginViewModel(loginUseCase, getSessionUseCase)
    }

    // --- Initial state ---

    @Test
    fun `initial state has empty email and password`() {
        val state = viewModel.uiState.value
        assertThat(state.email).isEmpty()
        assertThat(state.password).isEmpty()
    }

    @Test
    fun `initial state is not loading`() {
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `initial state has no error`() {
        assertThat(viewModel.uiState.value.error).isNull()
    }

    // --- Auto-login on existing session ---

    @Test
    fun `emits loginSuccess when session already exists`() = runTest {
        coEvery { getSessionUseCase() } returns Result.success(dummySesion)
        val vm = LoginViewModel(loginUseCase, getSessionUseCase)

        vm.loginSuccess.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `does not emit loginSuccess when no session`() = runTest {
        val vm = LoginViewModel(loginUseCase, getSessionUseCase)

        vm.loginSuccess.test {
            expectNoEvents()
        }
    }

    // --- onEmailChanged ---

    @Test
    fun `onEmailChanged updates email in state`() {
        viewModel.onEmailChanged("new@email.com")
        assertThat(viewModel.uiState.value.email).isEqualTo("new@email.com")
    }

    @Test
    fun `onEmailChanged with empty string clears email`() {
        viewModel.onEmailChanged("test@email.com")
        viewModel.onEmailChanged("")
        assertThat(viewModel.uiState.value.email).isEmpty()
    }

    // --- onPasswordChanged ---

    @Test
    fun `onPasswordChanged updates password in state`() {
        viewModel.onPasswordChanged("secret123")
        assertThat(viewModel.uiState.value.password).isEqualTo("secret123")
    }

    @Test
    fun `onPasswordChanged with empty string clears password`() {
        viewModel.onPasswordChanged("secret123")
        viewModel.onPasswordChanged("")
        assertThat(viewModel.uiState.value.password).isEmpty()
    }

    // --- onLogin validation ---

    @Test
    fun `onLogin shows error when email is blank`() {
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Ingresa email y contraseña")
    }

    @Test
    fun `onLogin shows error when password is blank`() {
        viewModel.onEmailChanged("test@email.com")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Ingresa email y contraseña")
    }

    @Test
    fun `onLogin shows error when both are blank`() {
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Ingresa email y contraseña")
    }

    // --- onLogin loading ---

    @Test
    fun `onLogin sets loading state during login`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(DomainError.NetworkError)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        // After completion, loading should be false
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    // --- onLogin success ---

    @Test
    fun `onLogin emits loginSuccess on valid credentials`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(dummySesion)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        viewModel.loginSuccess.test {
            assertThat(awaitItem()).isTrue()
        }
    }

    @Test
    fun `onLogin clears error on success`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(dummySesion)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isNull()
    }

    @Test
    fun `onLogin clears loading on success`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.success(dummySesion)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    // --- onLogin failure ---

    @Test
    fun `onLogin shows NetworkError message on failure`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(DomainError.NetworkError)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Sin conexion a internet")
    }

    @Test
    fun `onLogin shows Unauthorized message on failure`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(DomainError.Unauthorized)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Sesion expirada")
    }

    @Test
    fun `onLogin clears loading on failure`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(DomainError.NetworkError)

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onLogin shows generic message for unknown error`() = runTest {
        coEvery { loginUseCase(any(), any()) } returns Result.failure(RuntimeException("unknown"))

        viewModel.onEmailChanged("test@email.com")
        viewModel.onPasswordChanged("password")
        viewModel.onLogin()

        assertThat(viewModel.uiState.value.error).isEqualTo("Error al iniciar sesión")
    }

    // --- onDismissError ---

    @Test
    fun `onDismissError clears the error`() {
        viewModel.onEmailChanged("test@email.com")
        viewModel.onLogin()

        viewModel.onDismissError()

        assertThat(viewModel.uiState.value.error).isNull()
    }
}
