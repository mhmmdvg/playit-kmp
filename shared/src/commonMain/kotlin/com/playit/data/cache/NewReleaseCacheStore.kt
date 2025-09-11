package com.playit.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.playit.domain.models.NewReleasesCache
import com.playit.domain.models.NewReleasesResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class NewReleasesCacheStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val NEW_RELEASES_DATA_KEY = stringPreferencesKey("new_releases_data")
        private val NEW_RELEASES_TIMESTAMP_KEY = longPreferencesKey("new_releases_timestamp")
    }

    suspend fun saveNewReleases(data: NewReleasesResponse, timestamp: Long) {
        try {
            val json = Json.encodeToString(NewReleasesResponse.serializer(), data)

            dataStore.edit { preferences ->
                preferences[NEW_RELEASES_DATA_KEY] = json
                preferences[NEW_RELEASES_TIMESTAMP_KEY] = timestamp
            }
        } catch (error: Exception) {
            error.printStackTrace()
            println("Failed to save cache: ${error.message}")
        }
    }

    suspend fun loadNewReleases(): NewReleasesCache? {
        val preferences = dataStore.data.first()
        val dataString = preferences[NEW_RELEASES_DATA_KEY]
        val timestamp = preferences[NEW_RELEASES_TIMESTAMP_KEY]

        return if (dataString != null) {
            try {
                val data = Json.decodeFromString(NewReleasesResponse.serializer(), dataString)
                NewReleasesCache(
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
            preferences.remove(NEW_RELEASES_DATA_KEY)
            preferences.remove(NEW_RELEASES_TIMESTAMP_KEY)
        }
    }
}