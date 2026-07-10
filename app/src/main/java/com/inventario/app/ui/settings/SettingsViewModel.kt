package com.inventario.app.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.SettingsDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val userName: String = "",
    val userEmail: String = "",
    val userRol: String = "",
    val currentWs: String = "",
    val currentOrg: String = "",
    val darkModeEnabled: Boolean? = null,
    val useDynamicColor: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore,
    private val workspaceDataStore: WorkspaceDataStore,
    private val authDataStore: AuthDataStore,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableSharedFlow<Boolean>()
    val logoutEvent: SharedFlow<Boolean> = _logoutEvent.asSharedFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsDataStore.isDarkMode.collect { value ->
                _uiState.update { it.copy(darkModeEnabled = value) }
            }
        }
        viewModelScope.launch {
            settingsDataStore.useDynamicColor.collect { value ->
                _uiState.update { it.copy(useDynamicColor = value) }
            }
        }
        viewModelScope.launch {
            workspaceDataStore.wsNombre.collect { value ->
                _uiState.update { it.copy(currentWs = value) }
            }
        }
        viewModelScope.launch {
            workspaceDataStore.orgNombre.collect { value ->
                _uiState.update { it.copy(currentOrg = value) }
            }
        }
        viewModelScope.launch {
            authDataStore.userName.collect { value ->
                _uiState.update { it.copy(userName = value ?: "") }
            }
        }
        viewModelScope.launch {
            authDataStore.userEmail.collect { value ->
                _uiState.update { it.copy(userEmail = value ?: "") }
            }
        }
        viewModelScope.launch {
            authDataStore.userRol.collect { value ->
                _uiState.update { it.copy(userRol = value ?: "") }
            }
        }
    }

    fun onDarkModeChanged(enabled: Boolean?) {
        viewModelScope.launch { settingsDataStore.setDarkMode(enabled) }
    }

    fun onDynamicColorChanged(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setUseDynamicColor(enabled) }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
            _logoutEvent.emit(true)
        }
    }
}
