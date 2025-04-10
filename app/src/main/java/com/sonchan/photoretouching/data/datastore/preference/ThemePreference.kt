package com.sonchan.photoretouching.data.datastore.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val THEME_PREFERENCES_NAME = "theme_preferences"
private val Context.datastore by preferencesDataStore(name = THEME_PREFERENCES_NAME)

class ThemePreference(private val context: Context) {
    private val DARK_MODE_KEY = booleanPreferencesKey(name = THEME_PREFERENCES_NAME)

    val isDarkTheme: Flow<Boolean> = context.datastore.data
        .map { preferences -> preferences[DARK_MODE_KEY] ?: false }

    suspend fun setDarkTheme(isDark: Boolean){
        context.datastore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDark
        }
    }
}