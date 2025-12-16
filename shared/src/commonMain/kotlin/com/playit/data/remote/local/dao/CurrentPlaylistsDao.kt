package com.playit.data.remote.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.playit.data.remote.local.entities.OwnerEntity
import com.playit.data.remote.local.entities.PlaylistEntity
import com.playit.data.remote.local.entities.PlaylistImageEntity
import com.playit.data.remote.local.relations.CurrentPlaylistsWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistsDao {
    /* Insert Operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylists(playlists: List<PlaylistEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwner(owner: OwnerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwners(owners: List<OwnerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistImage(image: PlaylistImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistImages(images: List<PlaylistImageEntity>)

    /* Query Operations with Relations */
    @Transaction
    @Query("SELECT * FROM playlists")
    fun getPlaylistsWithDetails(): Flow<List<CurrentPlaylistsWithDetails>>

    @Transaction
    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistWithDetailsById(playlistId: String): Flow<CurrentPlaylistsWithDetails?>

    @Query("SELECT * FROM playlists")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun getPlaylistById(playlistId: String): Flow<PlaylistEntity?>

    /* Delete Operations */
    @Query("DELETE FROM playlists")
    suspend fun deleteAllPlaylists()

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: String)

    @Query("DELETE FROM owners WHERE id NOT IN (SELECT DISTINCT ownerId FROM playlists)")
    suspend fun deleteOrphanOwners()
}