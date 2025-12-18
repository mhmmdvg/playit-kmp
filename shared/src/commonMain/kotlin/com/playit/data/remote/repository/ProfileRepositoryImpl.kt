@file:OptIn(ExperimentalTime::class)

package com.playit.data.remote.repository

import com.playit.data.remote.api.ProfileApi
import com.playit.data.remote.local.dao.ProfileDao
import com.playit.data.remote.local.mappers.toDomain
import com.playit.data.remote.local.mappers.toProfileEntity
import com.playit.data.remote.local.mappers.toProfileImageEntities
import com.playit.domain.models.ProfileResponse
import com.playit.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class ProfileRepositoryImpl(
    private val profileApi: ProfileApi,
    private val profileDao: ProfileDao
//    private val profileCacheStore: ProfileCacheStore
) : ProfileRepository {
    private var _lastFetchTime = 0L
    private val _cacheExpiration = 60.minutes.inWholeMilliseconds

    override fun getCurrentProfile(): Flow<Result<ProfileResponse>> = flow {
        val dbFlow = profileDao.getProfileDetail()
            .map { relations ->
                val domainProfile = relations.toDomain()

                println("Repo Level $domainProfile")

                Result.success(domainProfile)
            }

        emitAll(dbFlow.onStart {
            if (shouldFetchFromNetwork()) {
                refreshProfile()
            }
        })
    }

    override suspend fun invalidateCache() {
        profileDao.deleteProfile()
    }

    private suspend fun refreshProfile() {
        runCatching {
            val response = profileApi.getCurrentProfile()
            println("Response $response")
            cacheProfile(response)
            _lastFetchTime = Clock.System.now().toEpochMilliseconds()
        }.onFailure { error ->
            println("Profile refresh failed: $error")
        }
    }

    private suspend fun cacheProfile(profile: ProfileResponse) {
        val profileEntities = profile.toProfileEntity()
        val imageEntities = profile.toProfileImageEntities()

        profileDao.insertProfile(profileEntities)
        if (imageEntities.isNotEmpty()) {
            profileDao.insertProfileImages(imageEntities)
        }
    }

    private fun shouldFetchFromNetwork(): Boolean {
        return (Clock.System.now().toEpochMilliseconds() - _lastFetchTime) > _cacheExpiration
    }

}