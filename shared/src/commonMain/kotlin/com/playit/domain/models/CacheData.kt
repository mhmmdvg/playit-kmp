package com.playit.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class CacheData<T>(
    val data: T,
    val timestamp: Long
)
