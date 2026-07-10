package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.CancelarVentaRequest
import com.inventario.app.data.remote.dto.VentaCreateRequest
import com.inventario.app.data.remote.dto.VentaDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VentaApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getVentas(
        token: String,
        orgSlug: String,
        wsSlug: String
    ): List<VentaDto> {
        return client.get("$orgSlug/$wsSlug/ventas") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun createVenta(
        token: String,
        orgSlug: String,
        wsSlug: String,
        request: VentaCreateRequest
    ): VentaDto {
        return client.post("$orgSlug/$wsSlug/ventas") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun cancelarVenta(
        token: String,
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: CancelarVentaRequest
    ): VentaDto {
        return client.post("$orgSlug/$wsSlug/ventas/$id/cancelar") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
