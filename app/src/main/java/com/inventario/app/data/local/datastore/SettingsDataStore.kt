package com.inventario.app.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val USE_DYNAMIC_COLOR_KEY = booleanPreferencesKey("dynamic_color")
    }

    val isDarkMode: Flow<Boolean?> = dataStore.data.map { it[DARK_MODE_KEY] }
    val useDynamicColor: Flow<Boolean> = dataStore.data.map { it[USE_DYNAMIC_COLOR_KEY] ?: false }

    suspend fun setDarkMode(enabled: Boolean?) {
        dataStore.edit { prefs ->
            if (enabled != null) prefs[DARK_MODE_KEY] = enabled
            else prefs.remove(DARK_MODE_KEY)
        }
    }

    suspend fun setUseDynamicColor(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[USE_DYNAMIC_COLOR_KEY] = enabled
        }
    }
}
