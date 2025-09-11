package com.playit.data.remote.repository

import com.playit.data.cache.NewReleasesCacheStore
import com.playit.domain.models.NewReleasesCache
import com.playit.domain.models.NewReleasesResponse
import com.playit.domain.repository.AlbumsRepository
import com.playit.data.remote.api.AlbumsApi
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class AlbumsRepositoryImpl(
    private val albumsApi: AlbumsApi,
    private val newReleasesCacheStore: NewReleasesCacheStore
) : AlbumsRepository {
    private var _cacheExpiration = 30.minutes

    suspend fun getCachedData(): NewReleasesCache? = newReleasesCacheStore.loadNewReleases()

    override suspend fun getNewReleases(): Result<NewReleasesResponse> {
        val cachedData = newReleasesCacheStore.loadNewReleases()
        if (cachedData != null && cacheIsValid(cachedData.timestamp)) {
            return Result.success(cachedData.data)
        }

        return try {
            val res = albumsApi.getNewReleases()
            newReleasesCacheStore.saveNewReleases(
                data = res,
                timestamp = Clock.System.now().epochSeconds
            )
            Result.success(res)
        } catch (error: ClientRequestException) {
            val errorBody = error.response.body<String>()
            val errorResponse = Json.decodeFromString<Any>(errorBody)
            Result.failure(Exception(errorResponse.toString()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun invalidateCache() {
        newReleasesCacheStore.clearCache()
    }

    override fun cacheIsValid(timestamp: Long): Boolean {
        return Clock.System.now() - Instant.fromEpochSeconds(timestamp) < _cacheExpiration
    }
}