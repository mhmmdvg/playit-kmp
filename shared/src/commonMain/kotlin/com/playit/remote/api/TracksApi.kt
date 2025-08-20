package com.playit.remote.api

import com.playit.domain.models.SeveralTracks
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class TracksApi(
    private val httpClient: HttpClient
) {
    suspend fun getSeveralTracks(): SeveralTracks {
        return httpClient.get("tracks").body()
    }
}