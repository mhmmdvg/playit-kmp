package com.playit.data.remote.local.mappers

import com.playit.data.remote.local.entities.*
import com.playit.data.remote.local.relations.AlbumWithDetails
import com.playit.domain.models.Artist
import com.playit.domain.models.ExternalUrls
import com.playit.domain.models.Item
import com.playit.domain.models.NewReleasesExternalUrls
import com.playit.domain.models.NewReleasesImage
import com.playit.domain.models.Restrictions

fun Item.toAlbumEntity(): AlbumEntity {
    return AlbumEntity(
        id = this.id,
        albumType = this.albumType,
        totalTracks = this.totalTracks,
        href = this.href,
        name = this.name,
        releaseDate = this.releaseDate,
        releaseDatePrecision = this.releaseDatePrecision,
        type = this.type,
        uri = this.uri,
        spotifyUrl = this.externalUrls?.spotify,
        restrictionReason = this.restrictions?.reason,
    )
}

fun Artist.toArtistEntity(): ArtistEntity {
    return ArtistEntity(
        id = id,
        name = name,
        href = href,
        type = type,
        uri = uri,
        spotifyUrl = externalUrls?.spotify,
    )
}

fun Item.toAlbumImageEntities(): List<AlbumImageEntity> {
    return images.map { image ->
        AlbumImageEntity(
            albumId = id,
            url = image.url,
            height = image.height,
            width = image.width
        )
    }
}

fun Item.toAlbumMarketEntities(): List<AlbumMarketEntity> {
    return availableMarkets.map { marketCode ->
        AlbumMarketEntity(
            albumId = id,
            market = marketCode,
        )
    }
}

fun Item.toAlbumArtistCrossRefs(): List<AlbumArtistCrossRef> {
    return artists.map { artist ->
        AlbumArtistCrossRef(
            albumId = id,
            artistId = artist.id,
        )
    }
}


/* Entity to Domain Model Mappers */

fun AlbumWithDetails.toDomain(): Item {
    return Item(
        id = album.id,
        albumType = album.albumType,
        totalTracks = album.totalTracks,
        availableMarkets = markets.map { it.market },
        externalUrls = album.spotifyUrl?.let { NewReleasesExternalUrls(it) },
        href = album.href,
        images = images.map { imageEntity ->
            NewReleasesImage(
                url = imageEntity.url,
                height = imageEntity.height,
                width = imageEntity.width
            )
        },
        name = album.name,
        releaseDate = album.releaseDate,
        releaseDatePrecision = album.releaseDatePrecision,
        restrictions = album.restrictionReason?.let { reason ->
            Restrictions(reason)
        },
        type = album.type,
        uri = album.uri,
        artists = artists.map { artistEntity ->
            Artist(
                id = artistEntity.id,
                name = artistEntity.name,
                href = artistEntity.href,
                type = artistEntity.type,
                uri = artistEntity.uri,
                externalUrls = artistEntity.spotifyUrl?.let { spotifyUrl ->
                    ExternalUrls(spotifyUrl)
                }
            )
        }
    )
}

fun AlbumEntity.toDomainSimple(): Item {
    return Item(
        id = id,
        albumType = albumType,
        totalTracks = totalTracks,
        availableMarkets = emptyList(),
        externalUrls = spotifyUrl?.let { NewReleasesExternalUrls(it) },
        href = href,
        images = emptyList(),
        name = name,
        releaseDate = releaseDate,
        releaseDatePrecision = releaseDatePrecision,
        restrictions = restrictionReason?.let { Restrictions(it) },
        type = type,
        uri = uri,
        artists = emptyList()
    )
}