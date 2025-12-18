package com.playit.data.remote.local.mappers

import com.playit.data.remote.local.entities.ProfileEntity
import com.playit.data.remote.local.entities.ProfileImageEntity
import com.playit.data.remote.local.relations.ProfileDetails
import com.playit.domain.models.ExplicitContent
import com.playit.domain.models.ExternalUrls
import com.playit.domain.models.Followers
import com.playit.domain.models.Image
import com.playit.domain.models.ProfileResponse

fun ProfileResponse.toProfileEntity(): ProfileEntity {
    return ProfileEntity(
        id = this.id,
        displayName = this.displayName,
        email = this.email,
        externalUrls = this.externalUrls?.spotify,
        filterEnabled = this.explicitContent?.filterEnabled,
        filterLocked = this.explicitContent?.filterLocked,
        followersHref = this.followers.href,
        followersTotal = this.followers.total,
        href = this.href,
        product = this.product,
        type = this.type,
        uri = this.uri,
    )
}

fun ProfileResponse.toProfileImageEntities(): List<ProfileImageEntity> {
    return images?.map { img ->
        ProfileImageEntity(
            profileId = id,
            url = img.url,
            height = img.height,
            width = img.width
        )
    } ?: emptyList()
}

/* Entity to Domain Model */
fun ProfileDetails.toDomain(): ProfileResponse {
    return ProfileResponse(
        id = profile.id,
        displayName = profile.displayName,
        email = profile.email,
        explicitContent = ExplicitContent(
            filterEnabled = profile.filterEnabled ?: false,
            filterLocked = profile.filterLocked ?: false
        ),
        externalUrls = ExternalUrls(spotify = profile.externalUrls ?: ""),
        followers = Followers(
            href = profile.followersHref,
            total = profile.followersTotal
        ),
        images = images.map { imageEntity ->
            Image(
                height = imageEntity.height,
                url = imageEntity.url,
                width = imageEntity.width
            )
        },
        href = profile.href,
        type = profile.type ?: "",
        uri = profile.uri
    )
}