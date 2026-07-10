package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Workspace
import kotlinx.coroutines.flow.Flow

interface WorkspaceRepository {
    fun getWorkspaces(orgSlug: String): Flow<List<Workspace>>
}
