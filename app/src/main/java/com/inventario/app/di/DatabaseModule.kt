package com.inventario.app.di

import android.content.Context
import androidx.room.Room
import com.inventario.app.data.local.db.InventarioDatabase
import com.inventario.app.data.local.db.dao.CategoriaDao
import com.inventario.app.data.local.db.dao.MovimientoDao
import com.inventario.app.data.local.db.dao.ProductoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): InventarioDatabase {
        return Room.databaseBuilder(
            context,
            InventarioDatabase::class.java,
            "inventario_db"
        ).build()
    }

    @Provides
    fun provideProductoDao(db: InventarioDatabase): ProductoDao = db.productoDao()

    @Provides
    fun provideCategoriaDao(db: InventarioDatabase): CategoriaDao = db.categoriaDao()

    @Provides
    fun provideMovimientoDao(db: InventarioDatabase): MovimientoDao = db.movimientoDao()
}
