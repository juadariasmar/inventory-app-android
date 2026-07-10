package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.OrganizationApi
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Organization
import com.inventario.app.domain.repository.OrganizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationRepositoryImpl @Inject constructor(
    private val organizationApi: OrganizationApi,
    private val authDataStore: AuthDataStore,
    private val workspaceDataStore: WorkspaceDataStore
) : OrganizationRepository {

    override fun getOrganizations(): Flow<List<Organization>> = flow {
        val token = authDataStore.token.first()
            ?: throw DomainError.Unauthorized

        val orgs = organizationApi.getOrganizations(token)
        emit(orgs.map { it.toDomain() })
    }
}
