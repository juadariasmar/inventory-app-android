package com.inventario.app.domain.model

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoriaNombre: String?,
    val creadoEn: String
)
