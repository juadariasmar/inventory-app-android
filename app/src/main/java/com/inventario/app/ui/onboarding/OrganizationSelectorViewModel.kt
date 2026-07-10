package com.inventario.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inventario.app.domain.usecase.dashboard.GetOrganizationsUseCase
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
class OrganizationSelectorViewModel @Inject constructor(
    private val getOrganizations: GetOrganizationsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrganizationSelectorUiState())
    val uiState: StateFlow<OrganizationSelectorUiState> = _uiState.asStateFlow()

    init {
        loadOrganizations()
    }

    private fun loadOrganizations() {
        viewModelScope.launch {
            getOrganizations()
                .onStart {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Error al cargar organizaciones"
                        )
                    }
                }
                .collect { orgs ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            organizations = orgs,
                            error = null
                        )
                    }
                }
        }
    }

    fun onRetry() {
        loadOrganizations()
    }
}
