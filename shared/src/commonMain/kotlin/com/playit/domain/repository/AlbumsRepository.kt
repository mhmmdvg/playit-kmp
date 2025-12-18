package com.playit.domain.repository

import com.playit.domain.models.NewReleasesResponse
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
interface AlbumsRepository {
    fun getNewReleases(): Flow<Result<NewReleasesResponse>>
    suspend fun invalidateCache()
//    fun cacheIsValid(timestamp: Long): Boolean
}