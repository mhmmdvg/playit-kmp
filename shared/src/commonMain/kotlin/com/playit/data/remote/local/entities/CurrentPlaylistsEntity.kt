package com.playit.data.remote.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


@Entity(tableName = "owners")
data class OwnerEntity(
    @PrimaryKey val id: String,
    val displayName: String?,
    val externalUrl: String?,
    val href: String,
    val type: String,
    val uri: String
)

@OptIn(ExperimentalTime::class)
@Entity(
    tableName = "playlists",
    foreignKeys = [
        ForeignKey(
            entity = OwnerEntity::class,
            parentColumns = ["id"],
            childColumns = ["ownerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ownerId") ]
)
data class PlaylistEntity (
    @PrimaryKey val id: String,
    val ownerId: String,
    val name: String,
    val description: String?,
    val collaborative: Boolean,
    val href: String,
    val externalUrl: String?,
    val public: Boolean,
    val snapshotId: String,
    val type: String,
    val uri: String,

    val tracksHref: String?,
    val tracksTotal: Int,

    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
)

@Entity(
    tableName = "playlist_images",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("playlistId") ]
)
data class PlaylistImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Long = 0,
    val playlistId: String,
    val url: String,
    val height: Int?,
    val width: Int?
)