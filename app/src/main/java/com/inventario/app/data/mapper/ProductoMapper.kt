package com.inventario.app.data.mapper

import com.inventario.app.data.local.db.entity.ProductoEntity
import com.inventario.app.data.remote.dto.ProductoDto
import com.inventario.app.domain.model.Producto

fun ProductoDto.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        codigo = codigo,
        precio = precio,
        cantidad = cantidad,
        stockMinimo = stockMinimo,
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre,
        creadoEn = creadoEn ?: ""
    )
}

fun ProductoDto.toEntity(workspaceId: String): ProductoEntity {
    return ProductoEntity(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        codigo = codigo,
        precio = precio,
        cantidad = cantidad,
        stockMinimo = stockMinimo,
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre,
        workspaceId = workspaceId,
        creadoEn = creadoEn ?: ""
    )
}

fun ProductoEntity.toDomain(): Producto {
    return Producto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        codigo = codigo,
        precio = precio,
        cantidad = cantidad,
        stockMinimo = stockMinimo,
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre,
        creadoEn = creadoEn
    )
}

fun Producto.toDto(): ProductoDto {
    return ProductoDto(
        id = id,
        nombre = nombre,
        descripcion = descripcion,
        codigo = codigo,
        precio = precio,
        cantidad = cantidad,
        stockMinimo = stockMinimo,
        categoriaId = categoriaId,
        categoriaNombre = categoriaNombre,
        creadoEn = creadoEn
    )
}
