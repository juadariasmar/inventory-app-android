package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.DashboardDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getDashboardMetrics(
        orgSlug: String,
        wsSlug: String
    ): DashboardDto {
        return client.get("$orgSlug/$wsSlug/analisis").body()
    }
}
