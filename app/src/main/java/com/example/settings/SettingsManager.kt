package com.example.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsManager(private val context: Context) {
    private val AUTO_REPLY_KEY = booleanPreferencesKey("auto_reply")

    suspend fun isAutoReplyEnabled(): Boolean {
        return context.dataStore.data.map { prefs -> prefs[AUTO_REPLY_KEY] ?: false }.first()
    }

    suspend fun setAutoReplyEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[AUTO_REPLY_KEY] = enabled }
    }
}
