package com.playit.data.remote.repository

import com.playit.domain.models.SeveralTracks
import com.playit.domain.repository.TracksRepository
import com.playit.data.remote.api.TracksApi
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.json.Json

class TracksRepositoryImpl(
    private val tracksApi: TracksApi
) : TracksRepository {
    override suspend fun getSeveralTracks(): Result<SeveralTracks> {
        return try {
            val res = tracksApi.getSeveralTracks()
            Result.success(res)
        } catch(error: ClientRequestException) {
            val errorBody = error.response.body<String>()
            val errorResponse = Json.decodeFromString<Any>(errorBody)
            Result.failure(Exception(errorResponse.toString()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun invalidateCache() {
        TODO("Not yet implemented")
    }
}