package com.inventario.app.data.repository

import com.inventario.app.data.local.datastore.AuthDataStore
import com.inventario.app.data.mapper.toDomain
import com.inventario.app.data.remote.api.AuthApi
import com.inventario.app.domain.error.DomainError
import com.inventario.app.domain.model.Sesion
import com.inventario.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val authDataStore: AuthDataStore
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<Sesion> = runCatching {
        val response = authApi.signIn(email, password)
        val token = response.token ?: throw DomainError.NetworkError
        val sesion = response.toDomain() ?: throw DomainError.Unauthorized

        authDataStore.saveSession(
            token = token,
            userId = sesion.userId,
            email = sesion.email,
            name = sesion.nombre,
            rol = sesion.rol
        )

        sesion
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    401 -> throw DomainError.Unauthorized
                    403 -> throw DomainError.Forbidden
                    in 500..599 -> throw DomainError.ServerError(throwable.response.status.value)
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun getSession(): Result<Sesion> = runCatching {
        val token = authDataStore.token.first() ?: throw DomainError.Unauthorized
        val response = authApi.getSession(token)
        val user = response.user ?: throw DomainError.Unauthorized

        Sesion(
            userId = user.id,
            email = user.email,
            nombre = user.name ?: user.email,
            rol = user.rol ?: "USUARIO",
            estado = "ACTIVO",
            permisos = emptyList()
        )
    }.recoverCatching { throwable ->
        when (throwable) {
            is DomainError -> throw throwable
            is io.ktor.client.plugins.ResponseException -> {
                when (throwable.response.status.value) {
                    401 -> throw DomainError.Unauthorized
                    else -> throw DomainError.NetworkError
                }
            }
            else -> throw DomainError.NetworkError
        }
    }

    override suspend fun logout() {
        try {
            val token = authDataStore.token.first()
            if (token != null) {
                authApi.signOut(token)
            }
        } catch (_: Exception) {
        } finally {
            authDataStore.clearSession()
        }
    }

    override fun observeToken(): Flow<String?> {
        return authDataStore.token
    }
}
