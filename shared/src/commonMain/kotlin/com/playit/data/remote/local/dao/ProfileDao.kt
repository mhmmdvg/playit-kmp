package com.playit.data.remote.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.playit.data.remote.local.entities.ProfileEntity
import com.playit.data.remote.local.entities.ProfileImageEntity
import com.playit.data.remote.local.relations.ProfileDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    /* Insert Operations */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfileImages(images: List<ProfileImageEntity>)

    /* Query Operations */
    @Transaction
    @Query("SELECT * FROM profile")
    fun getProfileDetail(): Flow<ProfileDetails>

    @Query("SELECT * FROM profile")
    fun getProfile(): Flow<ProfileEntity>

    /* Delete Operations */
    @Query("DELETE FROM profile")
    suspend fun deleteProfile()
}