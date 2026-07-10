package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.OrganizationDto
import com.inventario.app.domain.model.Organization

fun OrganizationDto.toDomain(): Organization {
    return Organization(
        id = id,
        nombre = nombre,
        slug = slug,
        planStatus = planStatus ?: "ACTIVO"
    )
}
