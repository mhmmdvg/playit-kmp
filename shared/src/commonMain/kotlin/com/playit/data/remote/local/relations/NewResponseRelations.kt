package com.playit.data.remote.local.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.playit.data.remote.local.entities.AlbumArtistCrossRef
import com.playit.data.remote.local.entities.AlbumEntity
import com.playit.data.remote.local.entities.AlbumImageEntity
import com.playit.data.remote.local.entities.AlbumMarketEntity
import com.playit.data.remote.local.entities.ArtistEntity

data class AlbumWithDetails(
    @Embedded val album: AlbumEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val images: List<AlbumImageEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "albumId"
    )
    val markets: List<AlbumMarketEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = AlbumArtistCrossRef::class,
            parentColumn = "albumId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity>
)