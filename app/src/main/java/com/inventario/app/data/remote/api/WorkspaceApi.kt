package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.WorkspaceDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkspaceApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getWorkspaces(token: String, orgSlug: String, wsSlug: String): List<WorkspaceDto> {
        return client.get("$orgSlug/$wsSlug/workspaces") {
            header("Authorization", "Bearer $token")
        }.body()
    }
}
