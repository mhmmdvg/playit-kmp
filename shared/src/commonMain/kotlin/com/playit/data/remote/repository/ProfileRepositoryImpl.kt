@file:OptIn(ExperimentalTime::class)

package com.playit.data.remote.repository

import com.playit.data.cache.ProfileCacheStore
import com.playit.data.remote.api.ProfileApi
import com.playit.domain.models.CacheData
import com.playit.domain.models.ProfileResponse
import com.playit.domain.repository.ProfileRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.json.Json
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val profileCacheStore: ProfileCacheStore
) : ProfileRepository {
    private val _cacheExpiration = 60.minutes

    suspend fun getCachedData(): CacheData<ProfileResponse>? = profileCacheStore.loadProfile()

    override suspend fun getCurrentProfile(): Result<ProfileResponse> {
        val cachedData = profileCacheStore.loadProfile()
        if (cachedData != null && cacheIsValid(cachedData.timestamp)) {
            return Result.success(cachedData.data)
        }

        return runCatching {
            val res = profileApi.getCurrentProfile()
            profileCacheStore.saveProfile(
                data = res,
                timestamp = Clock.System.now().epochSeconds
            )
            res
        }.recoverCatching { error ->
            if (error is ClientRequestException) {
                val errorBody = error.response.body<String>()
                val errorResponse = Json.decodeFromString<Any>(errorBody)
                throw Exception(errorResponse.toString())
            } else {
                throw error
            }
        }
    }

    override suspend fun invalidateCache() {
        profileCacheStore.clearCache()
    }

    override fun cacheIsValid(timestamp: Long): Boolean {
        return Clock.System.now() - Instant.fromEpochSeconds(timestamp) < _cacheExpiration
    }
}