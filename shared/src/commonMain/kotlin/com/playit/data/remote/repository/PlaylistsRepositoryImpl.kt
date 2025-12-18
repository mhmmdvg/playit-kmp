@file:OptIn(ExperimentalTime::class)

package com.playit.data.remote.repository

import com.playit.data.remote.api.PlaylistsApi
import com.playit.data.remote.local.dao.PlaylistsDao
import com.playit.data.remote.local.entities.ResourceMetadataEntity
import com.playit.data.remote.local.mappers.toDomain
import com.playit.data.remote.local.mappers.toOwnerEntity
import com.playit.data.remote.local.mappers.toPlaylistEntity
import com.playit.data.remote.local.mappers.toPlaylistImageEntities
import com.playit.domain.models.CreatePlaylistRequest
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.domain.models.PlaylistItems
import com.playit.domain.repository.PlaylistsRepository
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

class PlaylistsRepositoryImpl(
    private val playlistsApi: PlaylistsApi,
    private val playlistsDao: PlaylistsDao
) : PlaylistsRepository {

    private val _resourceKey = "current_playlists"
    private val _cacheExpiration = 30.minutes.inWholeMilliseconds

    override fun getCurrentPlaylists(): Flow<Result<CurrentPlaylistsResponse>> = flow {
        val dbFlow = playlistsDao.getPlaylistsWithDetails()
            .map { relations ->
                val domainPlaylists = relations.map { it.toDomain() }

                val response = CurrentPlaylistsResponse(
                    href = "",
                    items = domainPlaylists,
                    limit = domainPlaylists.size,
                    next = null,
                    offset = 0,
                    previous = null,
                    total = domainPlaylists.size
                )
                Result.success(response)
            }

        emitAll(
            dbFlow.onStart {
                if (shouldFetchFromNetwork()) {
                    println("Fetching from network")
                    refreshPlaylists()
                }
            }
        )
    }.flowOn(Dispatchers.IO)

    private suspend fun refreshPlaylists() {
        try {
            val response = playlistsApi.getCurrentPlaylists()
            cachePlaylists(response.items)
        } catch (error: Exception) {
            println("Background sync failed: $error")
        }
    }


    override suspend fun createPlaylist(
        userId: String,
        request: CreatePlaylistRequest
    ): Result<PlaylistItems> {
        return runCatching {
            val response = playlistsApi.createPlaylist(
                userId = userId,
                request = request
            )
            cachePlaylists(listOf(response))
            response
        }.onFailure { error ->
            when (error) {
                is ClientRequestException -> {
                    println("Client error: ${error.response.status} $error")
                }

                is ServerResponseException -> {
                    println("Server error: ${error.response.status} $error")
                }

                is ConnectTimeoutException,
                is SocketTimeoutException -> {
                    println("Timeout error: $error")
                }

                else -> {
                    println("Unknown error: $error")
                }
            }
        }
    }

    private suspend fun cachePlaylists(playlists: List<PlaylistItems>) {
        val owners = playlists.map { it.owner.toOwnerEntity() }.distinctBy { it.id }

        val ownersWithName = owners.filter { !it.displayName.isNullOrBlank() }
        if (ownersWithName.isNotEmpty()) {
            playlistsDao.insertOwnersIgnore(ownersWithName)
        }

        val ownersWithoutNames = owners.filter { it.displayName.isNullOrBlank() }
        if (ownersWithoutNames.isNotEmpty()) {
            playlistsDao.insertOwnersIgnore(ownersWithoutNames)
        }

        val playlistEntities = playlists.map { it.toPlaylistEntity() }
        val imageEntities = playlists.flatMap { it.toPlaylistImageEntities() }

        playlistsDao.saveLastFetchTime(
            ResourceMetadataEntity(
                _resourceKey,
                Clock.System.now().toEpochMilliseconds()
            )
        )

        playlistsDao.insertPlaylists(playlistEntities)

        if (imageEntities.isNotEmpty()) {
            playlistsDao.insertPlaylistImages(imageEntities)
        }
    }

    private suspend fun shouldFetchFromNetwork(): Boolean {
        val lastFetch = playlistsDao.getLastFetchTime(_resourceKey) ?: 0L
        return (Clock.System.now().toEpochMilliseconds() - lastFetch) > _cacheExpiration
    }

    override suspend fun invalidateCache() {
        playlistsDao.deleteAllPlaylists()
        playlistsDao.deleteOrphanOwners()
    }
}
