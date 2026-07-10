package com.inventario.app.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.inventario.app.data.local.db.dao.CategoriaDao
import com.inventario.app.data.local.db.dao.MovimientoDao
import com.inventario.app.data.local.db.dao.ProductoDao
import com.inventario.app.data.local.db.entity.CategoriaEntity
import com.inventario.app.data.local.db.entity.MovimientoEntity
import com.inventario.app.data.local.db.entity.ProductoEntity

@Database(
    entities = [
        ProductoEntity::class,
        CategoriaEntity::class,
        MovimientoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class InventarioDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun movimientoDao(): MovimientoDao
}
