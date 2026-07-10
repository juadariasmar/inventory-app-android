package com.inventario.app.data.mapper

import com.inventario.app.data.local.db.entity.CategoriaEntity
import com.inventario.app.data.remote.dto.CategoriaDto
import com.inventario.app.domain.model.Categoria

fun CategoriaDto.toDomain(): Categoria {
    return Categoria(
        id = id,
        nombre = nombre,
        prefijo = prefijo
    )
}

fun CategoriaDto.toEntity(workspaceId: String): CategoriaEntity {
    return CategoriaEntity(
        id = id,
        nombre = nombre,
        prefijo = prefijo,
        workspaceId = workspaceId
    )
}

fun CategoriaEntity.toDomain(): Categoria {
    return Categoria(
        id = id,
        nombre = nombre,
        prefijo = prefijo
    )
}
