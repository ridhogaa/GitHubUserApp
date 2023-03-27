package com.ergea.githubuserapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class SettingsDataStoreManager(private val context: Context) {
    val getTheme: Flow<Boolean> = context.dataStore.data.map {
        it[SETTINGS_THEME_KEY] ?: false
    }

    suspend fun setTheme(condition: Boolean) {
        context.dataStore.edit {
            it[SETTINGS_THEME_KEY] = condition
        }
    }

    companion object {
        private const val DATASTORE_NAME = "settings_datastore_preferences"
        private val SETTINGS_THEME_KEY = booleanPreferencesKey("settings_theme_key")
        private val Context.dataStore by preferencesDataStore(
            name = DATASTORE_NAME
        )
    }
}