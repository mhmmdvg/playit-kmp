package com.playit.remote.repository

import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.repository.PlaylistsRepository
import com.playit.remote.api.PlaylistsApi
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.json.Json

class PlaylistsRepositoryImpl(
    private val playlistsApi: PlaylistsApi
) : PlaylistsRepository {
   override suspend fun getCurrentPlaylists(): Result<CurrentPlaylistsResponse> {
        return try {
            val res = playlistsApi.getCurrentPlaylists()
            Result.success(res)
        } catch (error: ClientRequestException) {
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