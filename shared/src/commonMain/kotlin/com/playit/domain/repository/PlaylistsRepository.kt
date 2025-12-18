package com.playit.domain.repository

import com.playit.domain.models.CreatePlaylistRequest
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.models.PlaylistItems
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    fun getCurrentPlaylists(): Flow<Result<CurrentPlaylistsResponse>>
    suspend fun createPlaylist(userId: String, request: CreatePlaylistRequest): Result<PlaylistItems>
    suspend fun invalidateCache()
}