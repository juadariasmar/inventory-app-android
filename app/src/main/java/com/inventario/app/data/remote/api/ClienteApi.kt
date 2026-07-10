package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.ClienteCreateRequest
import com.inventario.app.data.remote.dto.ClienteDto
import com.inventario.app.data.remote.dto.ClienteUpdateRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClienteApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getClientes(
        orgSlug: String,
        wsSlug: String
    ): List<ClienteDto> {
        return client.get("$orgSlug/$wsSlug/clientes").body()
    }

    suspend fun getClienteById(
        orgSlug: String,
        wsSlug: String,
        id: Int
    ): ClienteDto {
        return client.get("$orgSlug/$wsSlug/clientes/$id").body()
    }

    suspend fun createCliente(
        orgSlug: String,
        wsSlug: String,
        request: ClienteCreateRequest
    ): ClienteDto {
        return client.post("$orgSlug/$wsSlug/clientes") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateCliente(
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: ClienteUpdateRequest
    ): ClienteDto {
        return client.put("$orgSlug/$wsSlug/clientes/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
