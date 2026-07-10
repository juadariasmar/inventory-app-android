package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.CancelarVentaRequest
import com.inventario.app.data.remote.dto.VentaCreateRequest
import com.inventario.app.data.remote.dto.VentaDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
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
        orgSlug: String,
        wsSlug: String
    ): List<VentaDto> {
        return client.get("$orgSlug/$wsSlug/ventas").body()
    }

    suspend fun createVenta(
        orgSlug: String,
        wsSlug: String,
        request: VentaCreateRequest
    ): VentaDto {
        return client.post("$orgSlug/$wsSlug/ventas") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun cancelarVenta(
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: CancelarVentaRequest
    ): VentaDto {
        return client.post("$orgSlug/$wsSlug/ventas/$id/cancelar") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
