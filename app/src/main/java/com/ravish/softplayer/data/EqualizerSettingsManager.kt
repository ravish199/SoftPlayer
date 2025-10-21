package com.ravish.softplayer.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 1. FIX: The DataStore instance MUST be a top-level property.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "equalizer_settings")

class EqualizerSettingsManager(private val context: Context) {

    // Define Preference Keys
    companion object {
        val EQ_ENABLED_KEY = booleanPreferencesKey("eq_enabled")
        val EQ_BAND_LEVELS_KEY = stringPreferencesKey("eq_band_levels")
    }

    // --- Read Data ---

    // Flow to get the enabled state
    val eqEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[EQ_ENABLED_KEY] ?: true // Default to true for a better user experience
    }

    // Flow to get the band levels
    val eqBandLevelsFlow: Flow<List<Float>> = context.dataStore.data.map { preferences ->
        val levelsString = preferences[EQ_BAND_LEVELS_KEY]
        if (levelsString.isNullOrEmpty()) {
            emptyList() // Return empty list if no levels are saved
        } else {
            // Convert the comma-separated string back to a list of Floats
            try {
                levelsString.split(',').map { it.toFloat() }
            } catch (e: NumberFormatException) {
                // In case of corrupted data, return an empty list to prevent crashes
                emptyList()
            }
        }
    }

    // --- Write Data ---

    /**
     * Saves the enabled state of the equalizer.
     */
    suspend fun saveEqEnabled(isEnabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[EQ_ENABLED_KEY] = isEnabled
        }
    }

    /**
     * Saves the list of band levels.
     * 2. FIX: Use a non-nullable List<Float> for type safety and consistency.
     * @param bandLevels A list of float values representing the level of each band.
     */
    suspend fun saveBandLevels(bandLevels: List<Float>?) {
        // Convert the list of floats to a single comma-separated string
        bandLevels?.let {
            val levelsString = it.joinToString(",")
            context.dataStore.edit { settings ->
                settings[EQ_BAND_LEVELS_KEY] = levelsString
            }
        }
    }
}
