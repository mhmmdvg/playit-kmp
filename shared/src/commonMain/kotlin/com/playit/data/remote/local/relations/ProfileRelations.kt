package com.playit.data.remote.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.playit.data.remote.local.entities.ProfileEntity
import com.playit.data.remote.local.entities.ProfileImageEntity

data class ProfileDetails(
    @Embedded val profile: ProfileEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "profileId"
    )
    val images: List<ProfileImageEntity>
)