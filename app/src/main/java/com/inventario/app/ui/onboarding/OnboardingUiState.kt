package com.inventario.app.ui.onboarding

import com.inventario.app.domain.model.Organization
import com.inventario.app.domain.model.Workspace

data class OrganizationSelectorUiState(
    val isLoading: Boolean = false,
    val organizations: List<Organization> = emptyList(),
    val error: String? = null
)

data class WorkspaceSelectorUiState(
    val isLoading: Boolean = false,
    val workspaces: List<Workspace> = emptyList(),
    val error: String? = null
)
