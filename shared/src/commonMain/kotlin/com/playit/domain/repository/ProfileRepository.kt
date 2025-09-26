package com.playit.domain.repository

import com.playit.domain.models.ProfileResponse

interface ProfileRepository {
    suspend fun getCurrentProfile(): Result<ProfileResponse>
    suspend fun invalidateCache()
    fun cacheIsValid(timestamp: Long): Boolean
}