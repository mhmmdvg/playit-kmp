package com.playit.data.remote.repository

import com.playit.data.remote.api.AlbumsApi
import com.playit.data.remote.local.dao.AlbumsDao
import com.playit.data.remote.local.mappers.toAlbumArtistCrossRefs
import com.playit.data.remote.local.mappers.toAlbumEntity
import com.playit.data.remote.local.mappers.toAlbumImageEntities
import com.playit.data.remote.local.mappers.toAlbumMarketEntities
import com.playit.data.remote.local.mappers.toArtistEntity
import com.playit.data.remote.local.mappers.toDomain
import com.playit.domain.models.Albums
import com.playit.domain.models.Item
import com.playit.domain.models.NewReleasesResponse
import com.playit.domain.repository.AlbumsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class AlbumsRepositoryImpl(
    private val albumsApi: AlbumsApi,
    private val albumsDao: AlbumsDao
) : AlbumsRepository {
    private var _cacheValidityDuration = 30.minutes

    override suspend fun getNewReleases(): Flow<Result<NewReleasesResponse>> = flow {
        val cachedAlbums = albumsDao.getAlbumsWithDetails().first()
        val isCachedValid = cachedAlbums.isNotEmpty() && (Clock.System.now()
            .toEpochMilliseconds() - cachedAlbums.first().album.createdAt) < _cacheValidityDuration.inWholeMilliseconds

        if (isCachedValid) {
            println("Using cached new releases data")
            val domainAlbums = cachedAlbums.map { it.toDomain() }
            val cachedResponse = NewReleasesResponse(
                albums = Albums(
                    href = cachedAlbums.firstOrNull()?.album?.href ?: "",
                    items = domainAlbums,
                    limit = domainAlbums.size,
                    next = null,
                    offset = 0,
                    previous = null,
                    total = domainAlbums.size
                )
            )
            emit(Result.success(cachedResponse))
        } else {
            try {
                println("Fetching new releases data")
                val response = albumsApi.getNewReleases()

                cacheAlbums(response.albums.items)
                emit(Result.success(response))
            } catch (error: Exception) {
                if (cachedAlbums.isNotEmpty()) {
                    val domainAlbums = cachedAlbums.map { it.toDomain() }
                    val staleResponse = NewReleasesResponse(
                        albums = Albums(
                            href = cachedAlbums.firstOrNull()?.album?.href ?: "",
                            items = domainAlbums,
                            limit = domainAlbums.size,
                            next = null,
                            offset = 0,
                            previous = null,
                            total = domainAlbums.size
                        )
                    )
                    emit(Result.success(staleResponse))
                } else {
                    emit(Result.failure(error))
                }
            }
        }
    }

    private suspend fun cacheAlbums(items: List<Item>) {
        items.forEach { it ->
            albumsDao.insertAlbum(it.toAlbumEntity())

            val artists = it.artists.map { it.toArtistEntity()}
            if (artists.isNotEmpty()) {
                albumsDao.insertArtists(artists)
            }

            val crossRefs = it.toAlbumArtistCrossRefs()
            if (crossRefs.isNotEmpty()) {
                albumsDao.insertAlbumArtistCrossRefs(crossRefs)
            }

            val images = it.toAlbumImageEntities()
            if (images.isNotEmpty()) {
                albumsDao.insertAlbumImages(images)
            }

            val markets = it.toAlbumMarketEntities()
            if (markets.isNotEmpty()) {
                albumsDao.insertAlbumMarkets(markets)
            }
        }
    }

    override suspend fun invalidateCache() {
        albumsDao.clearAllAlbums()
        albumsDao.deleteOrphanedArtists()
    }
}