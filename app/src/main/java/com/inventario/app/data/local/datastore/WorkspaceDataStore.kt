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
class WorkspaceDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val ORG_SLUG_KEY = stringPreferencesKey("org_slug")
        private val WS_SLUG_KEY = stringPreferencesKey("ws_slug")
        private val WS_ID_KEY = stringPreferencesKey("ws_id")
        private val WS_NAME_KEY = stringPreferencesKey("ws_nombre")
        private val ORG_NAME_KEY = stringPreferencesKey("org_nombre")
    }

    val orgSlug: Flow<String> = dataStore.data.map { it[ORG_SLUG_KEY] ?: "" }
    val wsSlug: Flow<String> = dataStore.data.map { it[WS_SLUG_KEY] ?: "" }
    val wsId: Flow<String> = dataStore.data.map { it[WS_ID_KEY] ?: "" }
    val wsNombre: Flow<String> = dataStore.data.map { it[WS_NAME_KEY] ?: "" }
    val orgNombre: Flow<String> = dataStore.data.map { it[ORG_NAME_KEY] ?: "" }

    suspend fun selectWorkspace(
        orgSlug: String,
        orgNombre: String,
        wsSlug: String,
        wsId: String,
        wsNombre: String
    ) {
        dataStore.edit { prefs ->
            prefs[ORG_SLUG_KEY] = orgSlug
            prefs[ORG_NAME_KEY] = orgNombre
            prefs[WS_SLUG_KEY] = wsSlug
            prefs[WS_ID_KEY] = wsId
            prefs[WS_NAME_KEY] = wsNombre
        }
    }

    suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
