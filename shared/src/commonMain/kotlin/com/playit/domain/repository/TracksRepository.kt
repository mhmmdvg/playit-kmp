package com.playit.domain.repository

import com.playit.domain.models.SeveralTracks

interface TracksRepository {
    suspend fun getSeveralTracks(): Result<SeveralTracks>
    fun invalidateCache()
}