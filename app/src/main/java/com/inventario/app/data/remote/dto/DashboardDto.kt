package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    @SerialName("inventarioGeneral")
    val inventarioGeneral: InventarioGeneralDto? = null,
    @SerialName("resumen")
    val resumen: ResumenDto? = null,
    @SerialName("ventasPorDia")
    val ventasPorDia: List<VentaPorDiaDto> = emptyList(),
    @SerialName("ventasPorCategoria")
    val ventasPorCategoria: List<VentaPorCategoriaDto> = emptyList(),
    @SerialName("distribucionStock")
    val distribucionStock: List<DistribucionStockDto> = emptyList(),
    @SerialName("stockCritico")
    val stockCritico: List<StockCriticoDto> = emptyList(),
    @SerialName("stockAgotarse")
    val stockAgotarse: List<StockCriticoDto> = emptyList(),
    @SerialName("sinMovimientos")
    val sinMovimientos: List<StockCriticoDto> = emptyList(),
    @SerialName("altaRotacion")
    val altaRotacion: List<StockCriticoDto> = emptyList()
)

@Serializable
data class InventarioGeneralDto(
    val totalProductos: Int = 0,
    val totalUnidades: Int = 0,
    val valorInventario: Double = 0.0,
    @SerialName("stockBajo")
    val stockBajo: Int = 0
)

@Serializable
data class ResumenDto(
    val entradas: Int = 0,
    val salidas: Int = 0,
    val neto: Int = 0,
    val totalVentas: Double = 0.0,
    val cantidadVentas: Int = 0
)

@Serializable
data class VentaPorDiaDto(
    val fecha: String,
    val total: Double,
    val cantidad: Int
)

@Serializable
data class VentaPorCategoriaDto(
    val categoriaId: Int,
    val categoriaNombre: String,
    val cantidad: Int,
    val total: Double
)

@Serializable
data class DistribucionStockDto(
    val estado: String,
    val cantidad: Int
)

@Serializable
data class StockCriticoDto(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaNombre: String? = null
)

@Serializable
data class MovimientosResumenDto(
    val entradas: Int = 0,
    val salidas: Int = 0,
    val neto: Int = 0
)
