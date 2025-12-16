package com.playit.domain.repository

import com.playit.domain.models.CurrentPlaylistsResponse
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun getCurrentPlaylists(): Flow<Result<CurrentPlaylistsResponse>>
    suspend fun invalidateCache()
}