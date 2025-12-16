package com.playit.data.remote.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.playit.data.remote.local.entities.OwnerEntity
import com.playit.data.remote.local.entities.PlaylistEntity
import com.playit.data.remote.local.entities.PlaylistImageEntity

data class CurrentPlaylistsWithDetails(
    @Embedded val playlist: PlaylistEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "playlistId"
    )
    val images: List<PlaylistImageEntity>,

    @Relation(
        parentColumn = "ownerId",
        entityColumn = "id"
    )
    val owner: OwnerEntity?
)