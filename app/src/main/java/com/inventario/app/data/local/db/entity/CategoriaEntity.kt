package com.inventario.app.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val prefijo: String,
    val workspaceId: String,
    val lastSynced: Long = System.currentTimeMillis()
)
