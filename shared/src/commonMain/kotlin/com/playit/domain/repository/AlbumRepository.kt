package com.playit.domain.repository

import com.playit.domain.models.NewReleasesResponse

interface AlbumRepository {
    suspend fun getNewReleases(): Result<NewReleasesResponse>
    fun invalidateCache()
}