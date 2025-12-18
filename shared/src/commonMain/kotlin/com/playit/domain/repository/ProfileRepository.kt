package com.playit.domain.repository

import com.playit.domain.models.ProfileResponse
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getCurrentProfile(): Flow<Result<ProfileResponse>>
    suspend fun invalidateCache()
}