package com.inventario.app.domain.usecase.dashboard

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import javax.inject.Inject

class SelectWorkspaceUseCase @Inject constructor(
    private val workspaceDataStore: WorkspaceDataStore
) {
    suspend operator fun invoke(
        orgSlug: String,
        orgNombre: String,
        wsSlug: String,
        wsId: String,
        wsNombre: String
    ) {
        workspaceDataStore.selectWorkspace(orgSlug, orgNombre, wsSlug, wsId, wsNombre)
    }
}
