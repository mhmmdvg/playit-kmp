package com.playit.domain.repository

import com.playit.domain.models.CurrentPlaylistsResponse

interface PlaylistsRepository {
    suspend fun getCurrentPlaylists(): Result<CurrentPlaylistsResponse>
    fun invalidateCache()
}