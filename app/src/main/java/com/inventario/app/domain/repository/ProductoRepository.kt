package com.inventario.app.domain.repository

import androidx.paging.PagingData
import com.inventario.app.domain.model.Producto
import kotlinx.coroutines.flow.Flow

interface ProductoRepository {
    fun getProductosPaged(): Flow<PagingData<Producto>>
    fun getProductos(): Flow<List<Producto>>
    suspend fun getProductoById(id: Int): Result<Producto>
    suspend fun createProducto(producto: Producto): Result<Producto>
    suspend fun updateProducto(id: Int, producto: Producto): Result<Producto>
    suspend fun deleteProducto(id: Int): Result<Unit>
}
