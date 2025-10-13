package com.playit.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.playit.domain.models.CacheData
import com.playit.domain.models.ProfileResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class ProfileCacheStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        private val PROFILE_DATA_KEY = stringPreferencesKey("profile_data")
        private val PROFILE_TIMESTAMP_KEY = longPreferencesKey("profile_timestamp")
    }

    suspend fun saveProfile(data: ProfileResponse, timestamp: Long) {
        try {
            val json = Json.encodeToString(ProfileResponse.serializer(), data)

            dataStore.edit { preferences ->
                preferences[PROFILE_DATA_KEY] = json
                preferences[PROFILE_TIMESTAMP_KEY] = timestamp
            }
        } catch (error: Exception) {
            error.printStackTrace()
            println("Failed to save cache: ${error.message}")
        }
    }

    suspend fun loadProfile(): CacheData<ProfileResponse>? {
        val preferences = dataStore.data.first()
        val dataString = preferences[PROFILE_DATA_KEY]
        val timestamp = preferences[PROFILE_TIMESTAMP_KEY]

        return if (dataString != null) {
            try {
                val data = Json.decodeFromString(ProfileResponse.serializer(), dataString)
                CacheData(
                    data = data,
                    timestamp = timestamp ?: 0L
                )
            } catch (error: Exception) {
                error.printStackTrace()
                println("Failed to load cache: ${error.message}")
                null
            }
        } else {
            null
        }
    }

    suspend fun clearCache() {
        dataStore.edit { preferences ->
            preferences.remove(PROFILE_DATA_KEY)
            preferences.remove(PROFILE_TIMESTAMP_KEY)
        }
    }
}