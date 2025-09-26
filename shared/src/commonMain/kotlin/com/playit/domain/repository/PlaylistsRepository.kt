package com.playit.domain.repository

import com.playit.domain.models.CurrentPlaylistsResponse

interface PlaylistsRepository {
    suspend fun getCurrentPlaylists(): Result<CurrentPlaylistsResponse>
    suspend fun invalidateCache()
    fun cacheIsValid(timestamp: Long): Boolean
}