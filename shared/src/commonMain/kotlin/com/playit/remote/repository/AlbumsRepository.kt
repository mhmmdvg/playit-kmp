package com.playit.remote.repository

import com.playit.domain.models.NewReleasesResponse
import com.playit.remote.api.AlbumsApi
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class AlbumsRepository(
    private val albumsApi: AlbumsApi
) {
    private var _newReleaseCache: Pair<NewReleasesResponse, Instant>? = null
    private val cacheValidityDuration = 15.minutes

    suspend fun getNewReleases(): Result<NewReleasesResponse> {
        _newReleaseCache?.let { (data, timestamp) ->
            val now = Clock.System.now()
            if (now - timestamp < cacheValidityDuration) {
                return Result.success(data)
            }
        }

        return try {
            val res = albumsApi.getNewReleases()
            Result.success(res)
        } catch (error: ClientRequestException) {
            val errorBody = error.response.body<String>()
            val errorResponse = Json.decodeFromString<Any>(errorBody)
            Result.failure(Exception(errorResponse.toString()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun invalidateCache() {
        _newReleaseCache = null
    }
}