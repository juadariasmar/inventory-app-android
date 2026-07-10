package com.inventario.app.data.remote.dto

import com.inventario.app.domain.model.Sesion
import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val email: String,
    val password: String
)

@Serializable
data class SignInResponse(
    val redirect: Boolean = false,
    val token: String? = null,
    val user: UserDto? = null
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String? = null,
    val rol: String? = null
)

@Serializable
data class SessionResponse(
    val session: SessionDto? = null,
    val user: UserDto? = null
)

@Serializable
data class SessionDto(
    val userId: String,
    val expiresAt: String? = null
)

@Serializable
data class ForgotPasswordRequest(
    val email: String
)

fun SignInResponse.toDomain(): Sesion? {
    val user = this.user ?: return null
    return Sesion(
        userId = user.id,
        email = user.email,
        nombre = user.name ?: user.email,
        rol = user.rol ?: "USUARIO",
        estado = "ACTIVO",
        permisos = emptyList()
    )
}

@Serializable
data class OrganizationDto(
    val id: String,
    val nombre: String,
    val slug: String,
    val planStatus: String? = null
)

@Serializable
data class UserWithOrgsResponse(
    val organizations: List<OrgMembershipDto> = emptyList()
)

@Serializable
data class OrgMembershipDto(
    val organization: OrganizationDto? = null,
    val role: String? = null
)
