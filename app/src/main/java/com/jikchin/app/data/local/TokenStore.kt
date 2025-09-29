package com.jikchin.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private val Context.authDataStore by preferencesDataStore("auth_prefs")

class TokenStore(context: Context) {

    private val dataStore = context.applicationContext.authDataStore

    companion object {
        private val KEY_ACCESS = stringPreferencesKey("access_token")
        private val KEY_REFRESH = stringPreferencesKey("refresh_token")
    }

    fun save(access: String?, refresh: String?) = runBlocking {
        dataStore.edit { prefs ->
            if (access != null) prefs[KEY_ACCESS] = access else prefs.remove(KEY_ACCESS)
            if (refresh != null) prefs[KEY_REFRESH] = refresh else prefs.remove(KEY_REFRESH)
        }
    }

    fun clear() = runBlocking {
        dataStore.edit { it.clear() }
    }

    fun getAccessToken(): String? = runBlocking {
        dataStore.data.first()[KEY_ACCESS]
    }

    fun getRefreshToken(): String? = runBlocking {
        dataStore.data.first()[KEY_REFRESH]
    }
}