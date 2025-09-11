package com.playit.data.remote.api

import com.playit.domain.models.NewReleasesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AlbumsApi(
    private val httpClient: HttpClient
) {
    suspend fun getNewReleases(): NewReleasesResponse {
        return httpClient.get("browse/new-releases").body()
    }
}