package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.CategoriaCreateRequest
import com.inventario.app.data.remote.dto.CategoriaDto
import com.inventario.app.data.remote.dto.CategoriaUpdateRequest
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
class CategoriaApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getCategorias(
        orgSlug: String,
        wsSlug: String
    ): List<CategoriaDto> {
        return client.get("$orgSlug/$wsSlug/categorias").body()
    }

    suspend fun createCategoria(
        orgSlug: String,
        wsSlug: String,
        request: CategoriaCreateRequest
    ): CategoriaDto {
        return client.post("$orgSlug/$wsSlug/categorias") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateCategoria(
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: CategoriaUpdateRequest
    ): CategoriaDto {
        return client.put("$orgSlug/$wsSlug/categorias/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}
