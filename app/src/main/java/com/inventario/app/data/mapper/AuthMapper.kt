package com.inventario.app.data.mapper

import com.inventario.app.data.remote.dto.SignInResponse
import com.inventario.app.domain.model.Sesion

fun SignInResponse.toDomain(): Sesion? {
    val user = user ?: return null
    return Sesion(
        userId = user.id,
        email = user.email,
        nombre = user.name ?: user.email,
        rol = user.rol ?: "USUARIO",
        estado = "ACTIVO",
        permisos = emptyList()
    )
}
