package com.inventario.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaDto(
    val id: Int,
    val nombre: String,
    val prefijo: String
)

@Serializable
data class CategoriaCreateRequest(
    val nombre: String,
    val prefijo: String
)

@Serializable
data class CategoriaUpdateRequest(
    val nombre: String,
    val prefijo: String
)
