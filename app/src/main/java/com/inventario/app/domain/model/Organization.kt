package com.inventario.app.domain.model

data class Organization(
    val id: String,
    val nombre: String,
    val slug: String,
    val planStatus: String
)
