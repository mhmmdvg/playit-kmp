package com.playit.remote.api

import com.playit.domain.models.CurrentPlaylistsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class PlaylistsApi(
    private val client: HttpClient
) {

    suspend fun getCurrentPlaylists(): CurrentPlaylistsResponse {
        return client.get("me/playlists").body()
    }
}