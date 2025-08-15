package com.playit.remote.repository

import com.playit.domain.models.NewReleasesResponse
import com.playit.remote.api.AlbumsApi
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import kotlinx.serialization.json.Json

class AlbumsRepository(
    private val albumsApi: AlbumsApi
) {
    suspend fun getNewReleases(): Result<NewReleasesResponse> {
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
}