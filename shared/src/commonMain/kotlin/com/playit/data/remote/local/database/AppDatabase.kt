package com.playit.data.remote.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.playit.data.remote.local.dao.AlbumsDao
import com.playit.data.remote.local.dao.PlaylistsDao
import com.playit.data.remote.local.dao.ProfileDao
import com.playit.data.remote.local.entities.AlbumArtistCrossRef
import com.playit.data.remote.local.entities.AlbumEntity
import com.playit.data.remote.local.entities.AlbumImageEntity
import com.playit.data.remote.local.entities.AlbumMarketEntity
import com.playit.data.remote.local.entities.ArtistEntity
import com.playit.data.remote.local.entities.OwnerEntity
import com.playit.data.remote.local.entities.PlaylistEntity
import com.playit.data.remote.local.entities.PlaylistImageEntity
import com.playit.data.remote.local.entities.ProfileEntity
import com.playit.data.remote.local.entities.ProfileImageEntity
import com.playit.data.remote.local.entities.ResourceMetadataEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        /* Metadata */
        ResourceMetadataEntity::class,

        /* New Release */
        AlbumEntity::class,
        ArtistEntity::class,
        AlbumArtistCrossRef::class,
        AlbumImageEntity::class,
        AlbumMarketEntity::class,

        /* Current Playlist */
        OwnerEntity::class,
        PlaylistEntity::class,
        PlaylistImageEntity::class,

        /* Profile */
        ProfileEntity::class,
        ProfileImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumsDao
    abstract fun playlistsDao(): PlaylistsDao

    abstract fun profileDao(): ProfileDao

    companion object {
        const val DATABASE_NAME = "playit_database.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect class DatabaseDriveFactory {
    fun createDriver(): RoomDatabase.Builder<AppDatabase>
}

fun getDatabaseBuilder(driveFactory: DatabaseDriveFactory): AppDatabase {
    return driveFactory.createDriver()
        .addMigrations()
        .fallbackToDestructiveMigration(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}