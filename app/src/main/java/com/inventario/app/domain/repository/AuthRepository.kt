package com.inventario.app.domain.repository

import com.inventario.app.domain.model.Sesion
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Sesion>
    suspend fun getSession(): Result<Sesion>
    suspend fun forgotPassword(email: String): Result<Unit>
    suspend fun logout()
    fun observeToken(): Flow<String?>
}
