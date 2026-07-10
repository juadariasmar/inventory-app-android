package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductoDto(
    val id: Int,
    val nombre: String,
    val descripcion: String? = null,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    @SerialName("stockMinimo")
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoriaNombre: String? = null,
    @SerialName("creadoEn")
    val creadoEn: String? = null
)

@Serializable
data class ProductoCreateRequest(
    val nombre: String,
    val descripcion: String? = null,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    @SerialName("stockMinimo")
    val stockMinimo: Int,
    val categoriaId: Int
)

@Serializable
data class ProductoUpdateRequest(
    val nombre: String,
    val descripcion: String? = null,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    @SerialName("stockMinimo")
    val stockMinimo: Int,
    val categoriaId: Int
)

@Serializable
data class ProductoListResponse(
    val productos: List<ProductoDto> = emptyList(),
    val siguienteCursor: Int? = null,
    val total: Int = 0
)
