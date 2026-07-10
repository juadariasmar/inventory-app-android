package com.inventario.app.data.remote.interceptor

import com.inventario.app.data.local.datastore.AuthDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val authDataStore: AuthDataStore
) {
    suspend fun getToken(): String? {
        return authDataStore.getToken()
    }

    suspend fun saveToken(token: String) {
        authDataStore.saveToken(token)
    }

    suspend fun clearToken() {
        authDataStore.clearToken()
    }

    fun getTokenBlocking(): String? {
        return runBlocking { authDataStore.getToken() }
    }
}
