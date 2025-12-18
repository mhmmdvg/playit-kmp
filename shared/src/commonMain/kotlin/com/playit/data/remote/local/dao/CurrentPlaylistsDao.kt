package com.playit.data.remote.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.playit.data.remote.local.entities.OwnerEntity
import com.playit.data.remote.local.entities.PlaylistEntity
import com.playit.data.remote.local.entities.PlaylistImageEntity
import com.playit.data.remote.local.entities.ResourceMetadataEntity
import com.playit.data.remote.local.relations.CurrentPlaylistsWithDetails
import kotlinx.coroutines.flow.Flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Dao
interface PlaylistsDao {
    /* Insert Operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

    @Upsert
    suspend fun upsertOwner(owner: OwnerEntity)

    @Upsert
    suspend fun upsertOwners(owners: List<OwnerEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOwnersIgnore(owners: List<OwnerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistImage(image: PlaylistImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistImages(images: List<PlaylistImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLastFetchTime(metadata: ResourceMetadataEntity)

    // --- Atomic Transactions (USE THESE from your Repository) ---
    @OptIn(ExperimentalTime::class)
    @Transaction
    suspend fun insertPlaylistWithDetails(
        owner: OwnerEntity,
        playlist: PlaylistEntity,
        images: List<PlaylistImageEntity>,
        fetchTime: Long
    ) {
        upsertOwner(owner)
        insertPlaylist(playlist)
        insertPlaylistImages(images)
        saveLastFetchTime(
            ResourceMetadataEntity(
                resourceId = "current_playlists",
                lastFetchedAt = fetchTime
            )
        )
    }

    /* Query Operations with Relations */
    @Transaction
    @Query("SELECT * FROM playlists ORDER BY createdAt DESC")
    fun getPlaylistsWithDetails(): Flow<List<CurrentPlaylistsWithDetails>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithDetailsById(playlistId: String): Flow<CurrentPlaylistsWithDetails?>

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: String): Flow<PlaylistEntity?>

    @Query("SELECT lastFetchedAt FROM resource_metadata WHERE resourceId = :resourceId")
    suspend fun getLastFetchTime(resourceId: String): Long?

    /* Delete Operations */
    @Query("DELETE FROM playlists")
    suspend fun deleteAllPlaylists()

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: String)

    @Query("DELETE FROM owners WHERE id NOT IN (SELECT DISTINCT ownerId FROM playlists)")
    suspend fun deleteOrphanOwners()
}