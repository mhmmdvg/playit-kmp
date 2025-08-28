package com.playit.domain.repository

import com.playit.domain.models.NewReleasesResponse

interface AlbumsRepository {
    suspend fun getNewReleases(): Result<NewReleasesResponse>
    fun invalidateCache()
}