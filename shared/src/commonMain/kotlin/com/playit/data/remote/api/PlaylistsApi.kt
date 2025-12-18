package com.playit.data.remote.api

import com.playit.domain.models.CreatePlaylistRequest
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.models.PlaylistItems
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PlaylistsApi(
    private val client: HttpClient
) {
    suspend fun getCurrentPlaylists(): CurrentPlaylistsResponse {
        return client.get("me/playlists").body()
    }

    suspend fun createPlaylist(userId: String, request: CreatePlaylistRequest): PlaylistItems {
        return client.post("users/$userId/playlists") {
            contentType(ContentType.Application.Json)
//            setBody(mapOf(
//                "name" to name,
//                "description" to description,
//                "public" to isPublic
//            ))
            setBody(request)
        }.body()
    }
}