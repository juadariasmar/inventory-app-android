package com.inventario.app.domain.model

data class Notificacion(
    val id: Int,
    val tipo: String,
    val titulo: String,
    val mensaje: String?,
    val link: String?,
    val leida: Boolean,
    val creadoEn: String
)
