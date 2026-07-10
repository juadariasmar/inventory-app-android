package com.inventario.app.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.usecase.auth.ForgotPasswordUseCase
import com.inventario.app.domain.usecase.auth.GetSessionUseCase
import com.inventario.app.domain.usecase.auth.GoogleSignInUseCase
import com.inventario.app.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginSuccess = MutableSharedFlow<Boolean>()
    val loginSuccess: SharedFlow<Boolean> = _loginSuccess.asSharedFlow()

    init {
        viewModelScope.launch {
            val session = getSessionUseCase()
            if (session.isSuccess) {
                _loginSuccess.emit(true)
            }
        }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onLogin() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "Ingresa email y contraseña") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(state.email.trim(), state.password)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false) }
                    _loginSuccess.emit(true)
                },
                onFailure = { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al iniciar sesión"
                        )
                    }
                }
            )
        }
    }

    fun onDismissError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onForgotPassword(email: String) {
        viewModelScope.launch {
            forgotPasswordUseCase(email)
                .onSuccess { _uiState.update { it.copy(error = null) } }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
        }
    }

    fun onGoogleSignIn(activity: Activity) {
        viewModelScope.launch {
            googleSignInUseCase(activity)
                .onSuccess { _loginSuccess.emit(true) }
                .onFailure { error -> _uiState.update { it.copy(error = error.message) } }
        }
    }
}
