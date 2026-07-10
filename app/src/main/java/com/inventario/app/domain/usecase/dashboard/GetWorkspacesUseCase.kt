package com.inventario.app.domain.usecase.dashboard

import com.inventario.app.domain.model.Workspace
import com.inventario.app.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWorkspacesUseCase @Inject constructor(
    private val workspaceRepository: WorkspaceRepository
) {
    operator fun invoke(orgSlug: String): Flow<List<Workspace>> = workspaceRepository.getWorkspaces(orgSlug)
}
