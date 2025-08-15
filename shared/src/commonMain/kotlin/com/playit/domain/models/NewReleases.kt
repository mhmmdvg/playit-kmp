package com.playit.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewReleasesResponse(
    val albums: Albums
)

@Serializable
data class Albums(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String? = null,
    val offset: Int,
    val previous: String? = null,
    val total: Int
)

@Serializable
data class Item(
    @SerialName("album_type")
    val albumType: String,
    @SerialName("total_tracks")
    val totalTracks: Long,
    @SerialName("available_markets")
    val availableMarkets: List<String> = emptyList(),
    @SerialName("external_urls")
    val externalUrls: NewReleasesExternalUrls? = null,
    val href: String,
    val id: String,
    val images: List<NewReleasesImage> = emptyList(),
    val name: String,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("release_date_precision")
    val releaseDatePrecision: String? = null,
    val restrictions: Restrictions? = null,
    val type: String,
    val uri: String,
    val artists: List<Artist> = emptyList()
)

@Serializable
data class Restrictions(
    val reason: String? = null
)

@Serializable
data class Artist(
    @SerialName("external_urls")
    val externalUrls: ExternalUrls? = null,
    val href: String? = null,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

@Serializable
data class NewReleasesExternalUrls(
    val spotify: String? = null
)

@Serializable
data class NewReleasesImage(
    val url: String,
    val height: Int? = null,
    val width: Int? = null
)