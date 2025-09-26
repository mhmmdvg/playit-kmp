package com.playit.data.cache

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.playit.domain.models.CacheData
import com.playit.domain.models.CurrentPlaylistsResponse
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class PlaylistCacheStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val PLAYLIST_DATA_KEY = stringPreferencesKey("playlist_data")
        private val PLAYLIST_TIMESTAMP_KEY = longPreferencesKey("playlist_timestamp")
    }

    suspend fun savePlaylist(data: CurrentPlaylistsResponse, timestamp: Long) {
        try {
            val json = Json.encodeToString(CurrentPlaylistsResponse.serializer(), data)

            dataStore.edit { preferences ->
                preferences[PLAYLIST_DATA_KEY] = json
                preferences[PLAYLIST_TIMESTAMP_KEY] = timestamp
            }
        } catch (error: Exception) {
            error.printStackTrace()
            println("Failed to save cache: ${error.message}")
        }
    }

    suspend fun loadPlaylist(): CacheData<CurrentPlaylistsResponse>? {
        val preferences = dataStore.data.first()
        val dataString = preferences[PLAYLIST_DATA_KEY]
        val timestamp = preferences[PLAYLIST_TIMESTAMP_KEY]

        return if (dataString != null) {
            try {
                val data = Json.decodeFromString(CurrentPlaylistsResponse.serializer(), dataString)
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
            preferences.remove(PLAYLIST_DATA_KEY)
            preferences.remove(PLAYLIST_TIMESTAMP_KEY)
        }
    }
}