package com.playit.data.remote.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: String,
    val displayName: String?,
    val email: String?,
    val filterEnabled: Boolean?,
    val filterLocked: Boolean?,
    val followersHref: String?,
    val followersTotal: Int,
    val externalUrls: String?,
    val href: String?,
    val product: String?,
    val type: String?,
    val uri: String?
)

@Entity(
    tableName = "profile_image",
    foreignKeys = [
        ForeignKey(
            entity = ProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("profileId")]
)
data class ProfileImageEntity(
    @PrimaryKey(autoGenerate = true) val imageId: Long = 0,
    val profileId: String,
    val url: String,
    val height: Int?,
    val width: Int?
)