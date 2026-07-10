package com.inventario.app.data.repository

import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.WorkspaceApi
import com.inventario.app.domain.model.Workspace
import com.inventario.app.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkspaceRepositoryImpl @Inject constructor(
    private val workspaceApi: WorkspaceApi
) : WorkspaceRepository {

    override fun getWorkspaces(orgSlug: String): Flow<List<Workspace>> = flow {
        val workspaces = workspaceApi.getWorkspaces(orgSlug, orgSlug)
        emit(workspaces.map { it.toDomain() })
    }
}
