package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Organization
import kotlinx.coroutines.flow.Flow

interface OrganizationRepository {
    fun getOrganizations(): Flow<List<Organization>>
}
