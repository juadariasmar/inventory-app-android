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
import kotlinx.coroutines.flow.combine
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
            combine(
                settingsDataStore.isDarkMode,
                settingsDataStore.useDynamicColor,
                workspaceDataStore.wsNombre,
                workspaceDataStore.orgNombre,
                authDataStore.userName,
                authDataStore.userEmail,
                authDataStore.userRol
            ) { darkMode, dynamicColor, ws, org, name, email, rol ->
                SettingsUiState(
                    userName = name ?: "",
                    userEmail = email ?: "",
                    userRol = rol ?: "",
                    currentWs = ws,
                    currentOrg = org,
                    darkModeEnabled = darkMode,
                    useDynamicColor = dynamicColor
                )
            }.collect { _uiState.value = it }
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
