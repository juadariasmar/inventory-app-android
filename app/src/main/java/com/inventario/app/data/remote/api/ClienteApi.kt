package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.ClienteCreateRequest
import com.inventario.app.data.remote.dto.ClienteDto
import com.inventario.app.data.remote.dto.ClienteUpdateRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
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
        token: String,
        orgSlug: String,
        wsSlug: String
    ): List<ClienteDto> {
        return client.get("$orgSlug/$wsSlug/clientes") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun getClienteById(
        token: String,
        orgSlug: String,
        wsSlug: String,
        id: Int
    ): ClienteDto {
        return client.get("$orgSlug/$wsSlug/clientes/$id") {
            header("Authorization", "Bearer $token")
        }.body()
    }

    suspend fun createCliente(
        token: String,
        orgSlug: String,
        wsSlug: String,
        request: ClienteCreateRequest
    ): ClienteDto {
        return client.post("$orgSlug/$wsSlug/clientes") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateCliente(
        token: String,
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: ClienteUpdateRequest
    ): ClienteDto {
        return client.put("$orgSlug/$wsSlug/clientes/$id") {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
