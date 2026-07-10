package com.inventario.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WorkspaceDto(
    val id: String,
    val nombre: String,
    val slug: String,
    val organizationId: String,
    val creadoEn: String? = null
)
