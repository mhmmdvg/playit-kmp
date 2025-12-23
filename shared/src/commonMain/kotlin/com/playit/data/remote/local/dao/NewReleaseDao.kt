package com.playit.data.remote.local.dao

import androidx.room.*
import com.playit.data.remote.local.entities.AlbumArtistCrossRef
import com.playit.data.remote.local.entities.AlbumEntity
import com.playit.data.remote.local.entities.AlbumImageEntity
import com.playit.data.remote.local.entities.AlbumMarketEntity
import com.playit.data.remote.local.entities.ArtistEntity
import com.playit.data.remote.local.entities.ResourceMetadataEntity
import com.playit.data.remote.local.relations.AlbumWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumsDao {
    /* Insert Operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbum(album: AlbumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbums(albums: List<AlbumEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtist(artist: ArtistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumArtistCrossRef(crossRef: AlbumArtistCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumArtistCrossRefs(crossRefs: List<AlbumArtistCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumImage(image: AlbumImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumImages(images: List<AlbumImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumMarket(market: AlbumMarketEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlbumMarkets(markets: List<AlbumMarketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastFetchTime(metadata: ResourceMetadataEntity)

    /* Query Operations with Relations */
    @Transaction
    @Query("SELECT * FROM albums")
    fun getAlbumsWithDetails(): Flow<List<AlbumWithDetails>>

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :albumId")
    fun getAlbumWithDetailsById(albumId: String): Flow<AlbumWithDetails?>

    @Query("SELECT * FROM albums")
    fun getAlbums(): Flow<List<AlbumEntity>>

    @Query("SELECT * FROM albums WHERE id = :albumId")
    fun getAlbumById(albumId: String): Flow<AlbumEntity?>

    @Query("SELECT lastFetchedAt FROM resource_metadata WHERE resourceId = :resourceId")
    suspend fun getLastFetchTime(resourceId: String): Long?

    /* Delete Operations */
    @Query("DELETE FROM albums")
    suspend fun clearAllAlbums()

    @Query("DELETE FROM albums WHERE id = :albumId")
    suspend fun deleteAlbumById(albumId: String)

    @Query("DELETE FROM artists WHERE id NOT IN (SELECT DISTINCT artistId FROM album_artist_cross_ref)")
    suspend fun deleteOrphanedArtists()
}