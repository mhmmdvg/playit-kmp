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
        return runCatching {
            tracksApi.getSeveralTracks()
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

    override fun invalidateCache() {
        TODO("Not yet implemented")
    }
}