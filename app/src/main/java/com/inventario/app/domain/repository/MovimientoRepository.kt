package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Movimiento
import com.inventario.app.domain.model.TipoMovimiento
import kotlinx.coroutines.flow.Flow

interface MovimientoRepository {
    fun getMovimientos(filtroTipo: TipoMovimiento? = null): Flow<List<Movimiento>>
    suspend fun registrarMovimiento(
        productoId: Int,
        tipo: TipoMovimiento,
        cantidad: Int,
        notas: String?
    ): Result<Movimiento>
}
