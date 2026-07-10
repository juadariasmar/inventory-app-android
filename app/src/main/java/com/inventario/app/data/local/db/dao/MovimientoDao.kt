package com.inventario.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.inventario.app.data.local.db.entity.MovimientoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {
    @Query("SELECT * FROM movimientos ORDER BY creadoEn DESC")
    fun getAll(): Flow<List<MovimientoEntity>>

    @Query("SELECT * FROM movimientos WHERE tipo = :tipo ORDER BY creadoEn DESC")
    fun getByTipo(tipo: String): Flow<List<MovimientoEntity>>

    @Insert
    suspend fun insert(movimiento: MovimientoEntity)

    @Query("DELETE FROM movimientos")
    suspend fun deleteAll()
}
