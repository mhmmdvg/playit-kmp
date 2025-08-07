package com.playit.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentPlaylistsResponse(
    val href: String,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int,
    val items: List<PlaylistItems>
)

@Serializable
data class PlaylistItems(
    val collaborative: Boolean,
    val description: String,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val owner: Owner,
    val public: Boolean,
    @SerialName("snapshot_id")
    val snapshotId: String,
    val tracks: Tracks,
    val type: String,
    val uri: String
)

@Serializable
data class ExternalUrls(
    val spotify: String
)

@Serializable
data class Image(
    val height: Int,
    val url: String,
    val width: Int
)

@Serializable
data class Owner(
    @SerialName("display_name")
    val displayName: String,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    val type: String,
    val uri: String
)

@Serializable
data class Tracks(
    val href: String,
    val total: Int
)
