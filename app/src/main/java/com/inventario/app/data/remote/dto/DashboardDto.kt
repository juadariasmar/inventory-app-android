package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DashboardDto(
    @SerialName("inventarioGeneral")
    val inventarioGeneral: List<InventarioGeneralItemDto> = emptyList(),
    @SerialName("stockAgotarse")
    val stockAgotarse: List<StockCriticoDto> = emptyList(),
    @SerialName("sinMovimientos")
    val sinMovimientos: List<StockCriticoDto> = emptyList(),
    @SerialName("altaRotacion")
    val altaRotacion: List<StockCriticoDto> = emptyList(),
    @SerialName("stockCritico")
    val stockCritico: List<StockCriticoDto> = emptyList(),
    @SerialName("resumen")
    val resumen: List<ResumenMovimientoDto> = emptyList(),
    @SerialName("ventasPorDia")
    val ventasPorDia: List<VentaPorDiaDto> = emptyList(),
    @SerialName("ventasPorCategoria")
    val ventasPorCategoria: List<VentaPorCategoriaDto> = emptyList(),
    @SerialName("distribucionStock")
    val distribucionStock: List<DistribucionStockDto> = emptyList()
)

@Serializable
data class InventarioGeneralItemDto(
    @SerialName("productoId")
    val productoId: Int = 0,
    val codigo: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val cantidad: Int = 0,
    @SerialName("stockMinimo")
    val stockMinimo: Int = 0,
    val precio: Double = 0.0,
    @SerialName("valorEnStock")
    val valorEnStock: Double = 0.0,
    val estado: String = "",
    @SerialName("diasDesdeUltimaActividad")
    val diasDesdeUltimaActividad: Int? = null
)

@Serializable
data class ResumenMovimientoDto(
    val fecha: String = "",
    val entradas: Int = 0,
    val salidas: Int = 0
)

@Serializable
data class VentaPorDiaDto(
    val fecha: String = "",
    @SerialName("numeroVentas")
    val numeroVentas: Int = 0,
    @SerialName("totalIngreso")
    val totalIngreso: Double = 0.0
)

@Serializable
data class VentaPorCategoriaDto(
    val categoria: String = "",
    @SerialName("unidadesVendidas")
    val unidadesVendidas: Int = 0,
    @SerialName("ingresoTotal")
    val ingresoTotal: Double = 0.0
)

@Serializable
data class DistribucionStockDto(
    val estado: String = "",
    val cantidad: Int = 0
)

@Serializable
data class StockCriticoDto(
    val id: Int = 0,
    val nombre: String = "",
    val codigo: String = "",
    val cantidad: Int = 0,
    @SerialName("stockMinimo")
    val stockMinimo: Int = 0,
    val categoriaNombre: String? = null
)
