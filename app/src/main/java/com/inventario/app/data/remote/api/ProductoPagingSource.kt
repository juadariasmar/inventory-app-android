package com.inventario.app.data.remote.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.domain.model.Producto

class ProductoPagingSource(
    private val api: ProductoApi,
    private val orgSlug: String,
    private val wsSlug: String
) : PagingSource<Int, Producto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Producto> {
        return try {
            val cursor = params.key
            val response = api.getProductos(
                orgSlug = orgSlug,
                wsSlug = wsSlug,
                cursor = cursor,
                limite = params.loadSize
            )
            LoadResult.Page(
                data = response.productos.map { it.toDomain() },
                prevKey = null,
                nextKey = if (response.productos.isNotEmpty()) (cursor ?: 0) + params.loadSize else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Producto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
