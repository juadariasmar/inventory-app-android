package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VentaDto(
    val id: Int,
    @SerialName("vendedorNombre")
    val vendedorNombre: String? = null,
    @SerialName("clienteNombre")
    val clienteNombre: String? = null,
    val total: Double,
    val notas: String? = null,
    val items: List<ItemVentaDto> = emptyList(),
    @SerialName("canceladaEn")
    val canceladaEn: String? = null,
    @SerialName("motivoCancelacion")
    val motivoCancelacion: String? = null,
    @SerialName("creadoEn")
    val creadoEn: String? = null
)

@Serializable
data class ItemVentaDto(
    val id: Int,
    val productoId: Int,
    val productoNombre: String? = null,
    val cantidad: Int,
    @SerialName("precioUnitario")
    val precioUnitario: Double,
    val subtotal: Double
)

@Serializable
data class VentaCreateRequest(
    @SerialName("clienteId")
    val clienteId: Int? = null,
    val items: List<VentaItemRequest>,
    val notas: String? = null
)

@Serializable
data class VentaItemRequest(
    val productoId: Int,
    val cantidad: Int
)

@Serializable
data class CancelarVentaRequest(
    val motivo: String
)
