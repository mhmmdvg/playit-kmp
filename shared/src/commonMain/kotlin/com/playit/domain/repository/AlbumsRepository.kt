package com.playit.domain.repository

import com.playit.domain.models.NewReleasesResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface AlbumsRepository {
    suspend fun getNewReleases(): Result<NewReleasesResponse>
    suspend fun invalidateCache()
    fun cacheIsValid(timestamp: Long): Boolean
}