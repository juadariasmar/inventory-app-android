package com.inventario.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movimientos",
    indices = [Index("workspaceId"), Index("productoId")]
)
data class MovimientoEntity(
    @PrimaryKey val id: Int,
    val productoId: Int,
    val productoNombre: String?,
    val tipo: String,
    val cantidad: Int,
    val notas: String?,
    val ventaId: Int?,
    val ordenCompraId: Int?,
    val workspaceId: String,
    val creadoEn: String,
    val usuarioNombre: String?,
    val lastSynced: Long = System.currentTimeMillis()
)
