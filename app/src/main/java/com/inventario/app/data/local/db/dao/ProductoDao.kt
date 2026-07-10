package com.inventario.app.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.inventario.app.data.local.db.entity.ProductoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun getAll(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE id = :id")
    suspend fun getById(id: Int): ProductoEntity?

    @Query("""
        SELECT * FROM productos
        WHERE nombre LIKE '%' || :query || '%' OR codigo LIKE '%' || :query || '%'
        ORDER BY nombre ASC
    """)
    fun search(query: String): Flow<List<ProductoEntity>>

    @Upsert
    suspend fun upsertAll(productos: List<ProductoEntity>)

    @Query("UPDATE productos SET cantidad = cantidad + :delta WHERE id = :productoId")
    suspend fun adjustStock(productoId: Int, delta: Int)

    @Query("DELETE FROM productos")
    suspend fun deleteAll()
}
