@file:OptIn(ExperimentalTime::class)

package com.playit.data.remote.repository

import com.playit.data.remote.api.PlaylistsApi
import com.playit.data.remote.local.dao.PlaylistsDao
import com.playit.data.remote.local.mappers.toDomain
import com.playit.data.remote.local.mappers.toOwnerEntity
import com.playit.data.remote.local.mappers.toPlaylistEntity
import com.playit.data.remote.local.mappers.toPlaylistImageEntities
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.models.PlaylistItems
import com.playit.domain.repository.PlaylistsRepository
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class PlaylistsRepositoryImpl(
    private val playlistsApi: PlaylistsApi,
    private val playlistsDao: PlaylistsDao
) : PlaylistsRepository {
    private val _cacheExpiration = 30.minutes

    override suspend fun getCurrentPlaylists(): Flow<Result<CurrentPlaylistsResponse>> = flow {
        val cachedPlaylists = playlistsDao.getPlaylistsWithDetails().first()
        val isCacheValid = cachedPlaylists.isNotEmpty() && (Clock.System.now()
            .toEpochMilliseconds() - cachedPlaylists.first().playlist.createdAt) < _cacheExpiration.inWholeMilliseconds

        if (isCacheValid) {
            val domainPlaylists = cachedPlaylists.map { it.toDomain() }
            val cachedResponse = CurrentPlaylistsResponse(
                href = cachedPlaylists.firstOrNull()?.playlist?.href ?: "",
                items = domainPlaylists,
                limit = domainPlaylists.size,
                next = null,
                offset = 0,
                previous = null,
                total = domainPlaylists.size
            )
            emit(Result.success(cachedResponse))
        } else {
            try {
                val res = playlistsApi.getCurrentPlaylists()

                cachePlaylists(res.items)
                emit(Result.success(res))
            } catch (error: Exception) {
                if (cachedPlaylists.isNotEmpty()) {
                    val domainPlaylists = cachedPlaylists.map { it.toDomain() }
                    val staleResponse = CurrentPlaylistsResponse(
                        href = cachedPlaylists.firstOrNull()?.playlist?.href ?: "",
                        items = domainPlaylists,
                        limit = domainPlaylists.size,
                        next = null,
                        offset = 0,
                        previous = null,
                        total = domainPlaylists.size
                    )
                    emit(Result.success(staleResponse))
                } else {
                    if (error is ClientRequestException) {
//                        val errorBody = error.response.body<String>()
//                        val errorResponse = Json.decodeFromString<Any>(errorBody)
                        emit(Result.failure(error))
                    } else {
                        throw error
                    }
                }
            }
        }
    }

    private suspend fun cachePlaylists(playlists: List<PlaylistItems>) {
        val distinctOwners = playlists.map { it.owner.toOwnerEntity() }.distinctBy { it.id }

        val playlistEntities = playlists.map { it.toPlaylistEntity() }
        val imageEntities = playlists.flatMap { it.toPlaylistImageEntities() }

        playlistsDao.insertOwners(distinctOwners)
        playlistsDao.insertPlaylists(playlistEntities)

        if (imageEntities.isNotEmpty()) {
            playlistsDao.insertPlaylistImages(imageEntities)
        }
    }

    override suspend fun invalidateCache() {
        playlistsDao.deleteAllPlaylists()
        playlistsDao.deleteOrphanOwners()
    }
}