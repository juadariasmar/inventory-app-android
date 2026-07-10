package com.inventario.app.data.repository

import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.OrganizationApi
import com.inventario.app.domain.model.Organization
import com.inventario.app.domain.repository.OrganizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationRepositoryImpl @Inject constructor(
    private val organizationApi: OrganizationApi
) : OrganizationRepository {

    override fun getOrganizations(): Flow<List<Organization>> = flow {
        val orgs = organizationApi.getOrganizations()
        emit(orgs.map { it.toDomain() })
    }
}
