package com.inventario.app.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.inventario.app.domain.model.Organization
import com.inventario.app.domain.model.Workspace
import com.inventario.app.ui.components.EmptyState
import com.inventario.app.ui.components.ErrorBanner
import com.inventario.app.ui.components.LoadingOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrganizationSelectorScreen(
    onOrgSelected: (String) -> Unit,
    viewModel: OrganizationSelectorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Inventory2,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "InventarioApp",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            ErrorBanner(
                error = uiState.error,
                onDismiss = viewModel::onRetry
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona tu empresa:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            when {
                uiState.isLoading -> {
                    LoadingOverlay(isLoading = true)
                }
                uiState.organizations.isEmpty() && uiState.error == null -> {
                    EmptyState(
                        icon = Icons.Filled.Business,
                        title = "Sin organizaciones",
                        description = "No tienes organizaciones asignadas. Contacta al administrador.",
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = uiState.organizations,
                            key = { it.id }
                        ) { org ->
                            OrganizationCard(
                                organization = org,
                                onClick = { onOrgSelected(org.slug) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrganizationCard(
    organization: Organization,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Organización: ${organization.nombre}" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Business,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = organization.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = organization.slug,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            PlanBadge(planStatus = organization.planStatus)
        }
    }
}

@Composable
private fun PlanBadge(planStatus: String) {
    val color = when (planStatus.uppercase()) {
        "PRO" -> MaterialTheme.colorScheme.primary
        "FREE" -> MaterialTheme.colorScheme.outline
        else -> MaterialTheme.colorScheme.secondary
    }

    Text(
        text = planStatus.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = Modifier
            .semantics { contentDescription = "Plan: $planStatus" }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkspaceSelectorScreen(
    onWsSelected: () -> Unit,
    viewModel: WorkspaceSelectorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigateToHome by viewModel.navigateToHome.collectAsState()

    LaunchedEffect(navigateToHome) {
        if (navigateToHome) {
            onWsSelected()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "InventarioApp",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* handled by nav back */ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            ErrorBanner(
                error = uiState.error,
                onDismiss = viewModel::onRetry
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Selecciona workspace:",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )

            when {
                uiState.isLoading -> {
                    LoadingOverlay(isLoading = true)
                }
                uiState.workspaces.isEmpty() && uiState.error == null -> {
                    EmptyState(
                        icon = Icons.Filled.FolderOpen,
                        title = "Sin workspaces",
                        description = "No hay workspaces disponibles para esta organización.",
                        modifier = Modifier.weight(1f)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = uiState.workspaces,
                            key = { it.id }
                        ) { ws ->
                            WorkspaceCard(
                                workspace = ws,
                                onClick = {
                                    viewModel.onWorkspaceSelected(
                                        workspace = ws,
                                        orgNombre = ws.organizationId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkspaceCard(
    workspace: Workspace,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Workspace: ${workspace.nombre}" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.FolderOpen,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = workspace.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = workspace.slug,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
