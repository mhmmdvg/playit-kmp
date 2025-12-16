package com.playit.data.remote.local.mappers

import com.playit.data.remote.local.entities.OwnerEntity
import com.playit.data.remote.local.entities.PlaylistEntity
import com.playit.data.remote.local.entities.PlaylistImageEntity
import com.playit.data.remote.local.relations.CurrentPlaylistsWithDetails
import com.playit.domain.models.ExternalUrls
import com.playit.domain.models.Image
import com.playit.domain.models.Owner
import com.playit.domain.models.PlaylistItems
import com.playit.domain.models.Tracks

fun PlaylistItems.toPlaylistEntity(): PlaylistEntity {
    return PlaylistEntity(
        id = this.id,
        name = this.name,
        ownerId = this.owner.id,
        description = this.description,
        href = this.href,
        externalUrl = this.externalUrls.spotify,
        type = this.type,
        uri = this.uri,
        tracksHref = this.tracks.href,
        tracksTotal = this.tracks.total,
        collaborative = this.collaborative,
        public = this.public,
        snapshotId = this.snapshotId,
    )
}

fun Owner.toOwnerEntity(): OwnerEntity {
    return OwnerEntity(
        id = id,
        displayName = displayName,
        externalUrl = externalUrls.spotify,
        href = href,
        type = type,
        uri = uri
    )
}

fun PlaylistItems.toPlaylistImageEntities(): List<PlaylistImageEntity> {
    return images.map { image ->
        PlaylistImageEntity(
            playlistId = id,
            url = image.url,
            height = image.height,
            width = image.width
        )
    }
}

/* Entity to Domain Model Mappers */
fun CurrentPlaylistsWithDetails.toDomain(): PlaylistItems {
    return PlaylistItems(
        collaborative = playlist.collaborative,
        description = playlist.description ?: "",
        externalUrls = ExternalUrls(spotify = playlist.externalUrl ?: ""),
        href = playlist.href,
        id = playlist.id,
        images = images.map { imageEntity ->
            Image(
                height = imageEntity.height,
                url = imageEntity.url,
                width = imageEntity.width
            )
        },
        name = playlist.name,
        owner = owner?.let { ownerEntity ->
            Owner(
                displayName = ownerEntity.displayName,
                externalUrls = ExternalUrls(spotify = ownerEntity.externalUrl ?: ""),
                href = ownerEntity.href,
                id = ownerEntity.id,
                type = ownerEntity.type,
                uri = ownerEntity.uri
            )
        } ?: Owner(
            displayName = null,
            externalUrls = ExternalUrls(spotify = ""),
            href = "",
            id = "",
            type = "",
            uri = ""
        ),
        public = playlist.public,
        snapshotId = playlist.snapshotId,
        tracks = Tracks(
            href = playlist.tracksHref ?: "",
            total = playlist.tracksTotal
        ),
        type = playlist.type,
        uri = playlist.uri
    )
}

fun PlaylistEntity.toDomainSimple(): PlaylistItems {
    return PlaylistItems(
        id = id,
        name = name,
        owner = Owner(
            id = ownerId,
            displayName = null,
            externalUrls = ExternalUrls(spotify = ""),
            href = "",
            type = "",
            uri = ""
        ),
        description = description ?: "",
        href = href,
        externalUrls = ExternalUrls(spotify = externalUrl ?: ""),
        type = type,
        uri = uri,
        tracks = Tracks(
            href = tracksHref ?: "",
            total = tracksTotal
        ),
        collaborative = collaborative,
        public = public,
        snapshotId = snapshotId,
        images = emptyList()
    )
}