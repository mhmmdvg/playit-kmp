package com.playit.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeveralTracks(
    val tracks: List<Track>
)

@Serializable
data class Track(
    val album: Albums,
    val artists: List<Artist>,
    @SerialName("available_markets")
    val availableMarkets: List<String>,
    @SerialName("disc_number")
    val discNumber: Int,
    @SerialName("duration_ms")
    val durationMs: Int,
    val explicit: Boolean,
    @SerialName("external_urls")
    val externalUrls: ExternalUrls,
    val href: String,
    val id: String,
    @SerialName("is_local")
    val isLocal: Boolean,
    @SerialName("is_playable")
    val isPlayable: Boolean,
    @SerialName("linked_from")
    val linkedFrom: Map<String, String>? = null,
    val restrictions: Restrictions? = null,
    val name: String,
    val popularity: Int,
    @SerialName("preview_url")
    val previewUrl: String? = null,
    @SerialName("track_number")
    val trackNumber: Int,
    val type: String,
    val uri: String,
)