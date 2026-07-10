package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.WorkspaceDataStore
import com.inventario.app.data.remote.api.OrganizationApi
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
    private val workspaceDataStore: WorkspaceDataStore
) : OrganizationRepository {

    override fun getOrganizations(): Flow<List<Organization>> = flow {
        val response = organizationApi.completeOnboarding()
        val orgSlug = response.orgSlug ?: throw Exception("No se encontró organización")

        val wsSlug = response.workspaceSlug
        if (!wsSlug.isNullOrBlank()) {
            workspaceDataStore.selectWorkspace(
                orgSlug = orgSlug,
                orgNombre = orgSlug,
                wsSlug = wsSlug,
                wsId = wsSlug,
                wsNombre = wsSlug
            )
        }

        emit(
            listOf(
                Organization(
                    id = orgSlug,
                    nombre = orgSlug,
                    slug = orgSlug,
                    planStatus = "FREE"
                )
            )
        )
    }
}
