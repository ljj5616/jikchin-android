package com.jikchin.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "auth_tokens")

class TokenStore(private val context: Context) {
    companion object {
        private val KEY_ACCESS = stringPreferencesKey("access")
        private val KEY_REFRESH = stringPreferencesKey("refresh")
    }

    suspend fun save(access: String, refresh: String) {
        context.dataStore.edit { pref ->
            pref[KEY_ACCESS] = access
            pref[KEY_REFRESH] = refresh
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getAccess(): String? =
        context.dataStore.data.map { it[KEY_ACCESS] }.first()

    suspend fun getRefresh(): String? =
        context.dataStore.data.map { it[KEY_REFRESH] }.first()
}