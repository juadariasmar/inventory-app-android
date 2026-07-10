package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.ClienteDto
import com.inventario.app.domain.model.Cliente

fun ClienteDto.toDomain(): Cliente {
    return Cliente(
        id = id,
        nombre = nombre,
        documento = documento,
        email = email,
        telefono = telefono,
        direccion = direccion,
        notas = notas
    )
}
