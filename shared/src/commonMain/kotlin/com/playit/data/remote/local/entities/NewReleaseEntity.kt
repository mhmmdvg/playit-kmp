package com.playit.data.remote.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Entity(tableName = "albums")
data class AlbumEntity @OptIn(ExperimentalTime::class) constructor(
    @PrimaryKey val id: String,
    val albumType: String,
    val totalTracks: Long,
    val href: String,
    val name: String,
    val releaseDate: String?,
    val releaseDatePrecision: String?,
    val type: String,
    val uri: String,
    val spotifyUrl: String?,
    val restrictionReason: String?,
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)

@Entity(tableName = "artists")
data class ArtistEntity(
    @PrimaryKey val id: String,
    val name: String,
    val href: String?,
    val type: String,
    val uri: String,
    val spotifyUrl: String?,
)

@Entity(
    tableName = "album_artist_cross_ref",
    primaryKeys = ["albumId", "artistId"],
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["id"],
            childColumns = ["artistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("albumId"), Index("artistId")]
)
data class AlbumArtistCrossRef(
    val albumId: String,
    val artistId: String
)

@Entity(
    tableName = "album_images",
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("albumId")]
)
data class AlbumImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Long = 0,
    val albumId: String,
    val url: String,
    val height: Int?,
    val width: Int?
)

@Entity(
    tableName = "album_markets",
    primaryKeys = ["albumId", "market"],
    foreignKeys = [
        ForeignKey(
            entity = AlbumEntity::class,
            parentColumns = ["id"],
            childColumns = ["albumId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("albumId")]
)
data class AlbumMarketEntity(
    val albumId: String,
    val market: String
)