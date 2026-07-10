package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.OrganizationDto
import com.inventario.app.data.remote.dto.UserWithOrgsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrganizationApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getOrganizations(token: String): List<OrganizationDto> {
        val response = client.get("usuarios/organizations") {
            header("Authorization", "Bearer $token")
        }.body<UserWithOrgsResponse>()
        return response.organizations.mapNotNull { it.organization }
    }
}
