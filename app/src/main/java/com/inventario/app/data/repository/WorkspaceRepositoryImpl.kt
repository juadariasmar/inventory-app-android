package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.WorkspaceApi
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Workspace
import com.inventario.app.domain.repository.WorkspaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkspaceRepositoryImpl @Inject constructor(
    private val workspaceApi: WorkspaceApi,
    private val authDataStore: AuthDataStore
) : WorkspaceRepository {

    override fun getWorkspaces(orgSlug: String): Flow<List<Workspace>> = flow {
        val token = authDataStore.token.first()
            ?: throw DomainError.Unauthorized

        val workspaces = workspaceApi.getWorkspaces(token, orgSlug, orgSlug)
        emit(workspaces.map { it.toDomain() })
    }
}
