package com.inventario.app.domain.usecase.dashboard

import com.inventario.app.domain.model.Organization
import com.inventario.app.domain.repository.OrganizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOrganizationsUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository
) {
    operator fun invoke(): Flow<List<Organization>> = organizationRepository.getOrganizations()
}
