@file:OptIn(ExperimentalTime::class)

package com.playit.data.remote.repository

import com.playit.data.cache.PlaylistCacheStore
import com.playit.data.remote.api.PlaylistsApi
import com.playit.domain.models.CacheData
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.repository.PlaylistsRepository
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class  PlaylistsRepositoryImpl(
    private val playlistsApi: PlaylistsApi,
    private val cacheStore: PlaylistCacheStore
) : PlaylistsRepository {
    private val _cacheExpiration = 30.minutes

    suspend fun getCachedData(): CacheData<CurrentPlaylistsResponse>? = cacheStore.loadPlaylist()

    override suspend fun getCurrentPlaylists(): Result<CurrentPlaylistsResponse> {
        val cachedData = cacheStore.loadPlaylist()
        if (cachedData != null && cacheIsValid(cachedData.timestamp)) {
            return Result.success(cachedData.data)
        }

        return runCatching {
            val res = playlistsApi.getCurrentPlaylists()
            cacheStore.savePlaylist(
                data = res,
                timestamp = Clock.System.now().epochSeconds
            )

            res
        }.recoverCatching { error ->
            if (error is ClientRequestException) {
                val errorBody = error.response.body<String>()
                val errorResponse = Json.decodeFromString<Any>(errorBody)
                throw Exception(errorResponse.toString())
            } else {
                throw error
            }
        }
    }

    override suspend fun invalidateCache() {
        cacheStore.clearCache()
    }

    override fun cacheIsValid(timestamp: Long): Boolean {
        return Clock.System.now() - Instant.fromEpochSeconds(timestamp) < _cacheExpiration
    }
}