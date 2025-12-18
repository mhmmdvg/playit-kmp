package com.playit.data.remote.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resource_metadata")
data class ResourceMetadataEntity(
    @PrimaryKey val resourceId: String,
    val lastFetchedAt: Long
)