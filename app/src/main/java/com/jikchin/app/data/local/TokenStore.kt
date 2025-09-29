package com.jikchin.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.dataStore by preferencesDataStore("auth_prefs")

class TokenStore(private val context: Context) {
    companion object {
        private val KEY_ACCESS = stringPreferencesKey("access_token")
        private val KEY_REFRESH = stringPreferencesKey("refresh_token")
    }

    fun save(access: String?, refresh: String?) = runBlocking {
        context.dataStore.edit { prefs ->
            if (access != null) prefs[KEY_ACCESS] = access else prefs.remove(KEY_ACCESS)
            if (refresh != null) prefs[KEY_REFRESH] = refresh else prefs.remove(KEY_REFRESH)
        }
    }

    fun clear() = runBlocking {
        context.dataStore.edit { it.clear() }
    }

    fun getAccessToken(): String? = runBlocking {
        context.dataStore.data.first()[KEY_ACCESS]
    }

    fun getRefreshToken(): String? = runBlocking {
        context.dataStore.data.first()[KEY_REFRESH]
    }
}