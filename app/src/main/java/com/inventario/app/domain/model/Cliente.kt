package com.inventario.app.domain.model

data class Cliente(
    val id: Int,
    val nombre: String,
    val documento: String?,
    val email: String?,
    val telefono: String?,
    val direccion: String?,
    val notas: String?
)
