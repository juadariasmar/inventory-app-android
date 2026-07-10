package com.inventario.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inventario.app.data.local.db.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAll(): Flow<List<CategoriaEntity>>

    @Upsert
    suspend fun upsertAll(categorias: List<CategoriaEntity>)

    @Query("DELETE FROM categorias")
    suspend fun deleteAll()
}
