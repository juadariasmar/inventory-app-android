package com.inventario.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String?,
    val codigo: String,
    val precio: Double,
    val cantidad: Int,
    val stockMinimo: Int,
    val categoriaId: Int,
    val categoriaNombre: String?,
    val workspaceId: String,
    val creadoEn: String,
    val lastSynced: Long = System.currentTimeMillis()
)
