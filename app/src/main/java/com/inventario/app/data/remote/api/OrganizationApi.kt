package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.OrganizationDto
import com.inventario.app.data.remote.dto.UserWithOrgsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getOrganizations(): List<OrganizationDto> {
        val response = client.get("usuarios/organizations").body<UserWithOrgsResponse>()
        return response.organizations.mapNotNull { it.organization }
    }
}
