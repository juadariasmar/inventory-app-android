package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.WorkspaceDto
import com.inventario.app.domain.model.Workspace

fun WorkspaceDto.toDomain(): Workspace {
    return Workspace(
        id = id,
        nombre = nombre,
        slug = slug,
        organizationId = organizationId
    )
}
