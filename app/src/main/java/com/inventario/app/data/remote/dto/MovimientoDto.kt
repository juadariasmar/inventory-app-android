package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovimientoDto(
    val id: Int,
    val productoId: Int,
    val productoNombre: String? = null,
    val tipo: String,
    val cantidad: Int,
    val notas: String? = null,
    val ventaId: Int? = null,
    val ordenCompraId: Int? = null,
    @SerialName("creadoEn")
    val creadoEn: String? = null,
    val usuarioNombre: String? = null
)

@Serializable
data class MovimientoCreateRequest(
    val productoId: Int,
    val tipo: String,
    val cantidad: Int,
    val notas: String? = null
)

@Serializable
data class MovimientoListResponse(
    val movimientos: List<MovimientoDto> = emptyList(),
    val siguienteCursor: Int? = null,
    val total: Int = 0
)
