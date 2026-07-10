package com.inventario.app.ui.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.model.Workspace
import com.inventario.app.domain.usecase.dashboard.GetWorkspacesUseCase
import com.inventario.app.domain.usecase.dashboard.SelectWorkspaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkspaceSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getWorkspaces: GetWorkspacesUseCase,
    private val selectWorkspace: SelectWorkspaceUseCase
) : ViewModel() {

    private val orgSlug: String = savedStateHandle["orgSlug"] ?: ""

    private val _uiState = MutableStateFlow(WorkspaceSelectorUiState())
    val uiState: StateFlow<WorkspaceSelectorUiState> = _uiState.asStateFlow()

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    init {
        loadWorkspaces()
    }

    private fun loadWorkspaces() {
        viewModelScope.launch {
            getWorkspaces(orgSlug)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar workspaces"
                        )
                    }
                }
                .collect { workspaces ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            workspaces = workspaces,
                            error = null
                        )
                    }
                }
        }
    }

    fun onWorkspaceSelected(workspace: Workspace, orgNombre: String) {
        viewModelScope.launch {
            selectWorkspace(
                orgSlug = orgSlug,
                orgNombre = orgNombre,
                wsSlug = workspace.slug,
                wsId = workspace.id,
                wsNombre = workspace.nombre
            )
            _navigateToHome.value = true
        }
    }

    fun onRetry() {
        loadWorkspaces()
    }
}
