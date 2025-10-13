package com.playit.data.remote.api

import com.playit.domain.models.ProfileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProfileApi(
    private val httpClient: HttpClient
) {
    suspend fun getCurrentProfile(): ProfileResponse {
        return httpClient.get("me").body()
    }
}