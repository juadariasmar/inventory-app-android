package com.inventario.app.domain.model

data class Workspace(
    val id: String,
    val nombre: String,
    val slug: String,
    val organizationId: String
)
