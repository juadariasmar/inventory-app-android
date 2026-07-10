package com.inventario.app.data.remote.dto

import kotlinx.serialization.SerialName
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
    @SerialName("rol")
    val rol: String? = null
)

@Serializable
data class SessionResponse(
    val session: SessionDto? = null,
    val user: UserDto? = null
)

@Serializable
data class SessionDto(
    val id: String? = null,
    val userId: String? = null,
    val expiresAt: String? = null
)

@Serializable
data class UserWithOrgsResponse(
    val user: UserDto? = null,
    val organizations: List<OrganizationMembershipDto> = emptyList()
)

@Serializable
data class OrganizationMembershipDto(
    val id: String,
    val role: String,
    val organizationId: String,
    val organization: OrganizationDto? = null
)

@Serializable
data class OrganizationDto(
    val id: String,
    val nombre: String,
    val slug: String,
    @SerialName("planStatus")
    val planStatus: String? = null,
    val workspaces: List<WorkspaceDto>? = null
)
