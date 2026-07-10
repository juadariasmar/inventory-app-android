package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Venta
import kotlinx.coroutines.flow.Flow

interface VentaRepository {
    fun getVentas(): Flow<List<Venta>>
    suspend fun getVentaById(id: Int): Result<Venta>
    suspend fun createVenta(
        clienteId: Int?,
        items: List<Pair<Int, Int>>, // productoId, cantidad
        notas: String?
    ): Result<Venta>
    suspend fun cancelarVenta(id: Int, motivo: String): Result<Unit>
}
