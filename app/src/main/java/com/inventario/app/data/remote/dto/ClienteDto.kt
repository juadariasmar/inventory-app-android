package com.inventario.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDto(
    val id: Int,
    val nombre: String,
    val documento: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)

@Serializable
data class ClienteCreateRequest(
    val nombre: String,
    val documento: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)

@Serializable
data class ClienteUpdateRequest(
    val nombre: String,
    val documento: String? = null,
    val email: String? = null,
    val telefono: String? = null,
    val direccion: String? = null,
    val notas: String? = null
)
