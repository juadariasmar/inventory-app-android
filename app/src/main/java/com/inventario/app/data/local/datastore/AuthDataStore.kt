package com.inventario.app.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
        private val USER_NAME_KEY = stringPreferencesKey("user_name")
        private val USER_ROL_KEY = stringPreferencesKey("user_rol")
    }

    val token: Flow<String?> = dataStore.data.map { it[TOKEN_KEY] }
    val userId: Flow<String?> = dataStore.data.map { it[USER_ID_KEY] }
    val userEmail: Flow<String?> = dataStore.data.map { it[USER_EMAIL_KEY] }
    val userName: Flow<String?> = dataStore.data.map { it[USER_NAME_KEY] }
    val userRol: Flow<String?> = dataStore.data.map { it[USER_ROL_KEY] }

    suspend fun getToken(): String? {
        return dataStore.data.first()[TOKEN_KEY]
    }

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun saveSession(
        token: String,
        userId: String,
        email: String,
        name: String,
        rol: String
    ) {
        dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USER_EMAIL_KEY] = email
            prefs[USER_NAME_KEY] = name
            prefs[USER_ROL_KEY] = rol
        }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    private suspend fun <T> DataStore<Preferences>.first(): Preferences {
        return dataStore.data.first()
    }
}
