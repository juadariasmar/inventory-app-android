package com.inventario.app.data.remote.api

import com.inventario.app.data.remote.dto.ProductoCreateRequest
import com.inventario.app.data.remote.dto.ProductoDto
import com.inventario.app.data.remote.dto.ProductoListResponse
import com.inventario.app.data.remote.dto.ProductoUpdateRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductoApi @Inject constructor(
    private val client: HttpClient
) {
    suspend fun getProductos(
        orgSlug: String,
        wsSlug: String,
        limite: Int = 50,
        cursor: Int? = null
    ): ProductoListResponse {
        val params = buildString {
            append("limite=$limite")
            cursor?.let { append("&cursor=$it") }
        }
        return client.get("$orgSlug/$wsSlug/productos?$params").body()
    }

    suspend fun createProducto(
        orgSlug: String,
        wsSlug: String,
        request: ProductoCreateRequest
    ): ProductoDto {
        return client.post("$orgSlug/$wsSlug/productos") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updateProducto(
        orgSlug: String,
        wsSlug: String,
        id: Int,
        request: ProductoUpdateRequest
    ): ProductoDto {
        return client.put("$orgSlug/$wsSlug/productos/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteProducto(
        orgSlug: String,
        wsSlug: String,
        id: Int
    ) {
        client.delete("$orgSlug/$wsSlug/productos/$id")
    }
}
