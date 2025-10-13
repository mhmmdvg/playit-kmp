package com.playit.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val country: String? = null,
    val displayName: String? = null,
    val email: String? = null,
    @SerialName("explicit_content")
    val explicitContent: ExplicitContent? = null,
    val externalUrls: ExternalUrls? = null,
    val followers: Followers,
    val href: String? = null,
    val id: String,
    val images: List<Image>,
    val product: String? = null,
    val type: String,
    val uri: String? = null
)

@Serializable
data class ExplicitContent(
    @SerialName("filter_enabled")
    val filterEnabled: Boolean,
    @SerialName("filter_locked")
    val filterLocked: Boolean
)

@Serializable
data class Followers(val href: String?, val total: Int)
