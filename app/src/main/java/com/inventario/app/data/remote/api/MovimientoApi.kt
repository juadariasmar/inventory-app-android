package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.MovimientoCreateRequest
import com.inventario.app.data.remote.dto.MovimientoDto
import com.inventario.app.data.remote.dto.MovimientoListResponse
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
class MovimientoApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getMovimientos(
        orgSlug: String,
        wsSlug: String,
        limite: Int = 50,
        cursor: Int? = null
    ): MovimientoListResponse {
        val params = buildString {
            append("limite=$limite")
            cursor?.let { append("&cursor=$it") }
        }
        return client.get("$orgSlug/$wsSlug/movimientos?$params").body()
    }

    suspend fun registrarMovimiento(
        orgSlug: String,
        wsSlug: String,
        request: MovimientoCreateRequest
    ): MovimientoDto {
        return client.post("$orgSlug/$wsSlug/movimientos") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
