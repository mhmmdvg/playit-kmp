package com.playit.data.remote.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.playit.data.remote.local.dao.AlbumsDao
import com.playit.data.remote.local.dao.PlaylistsDao
import com.playit.data.remote.local.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [
        AlbumEntity::class,
        ArtistEntity::class,
        AlbumArtistCrossRef::class,
        AlbumImageEntity::class,
        AlbumMarketEntity::class,

        OwnerEntity::class,
        PlaylistEntity::class,
        PlaylistImageEntity::class
    ],
    version = 1,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun albumsDao(): AlbumsDao
    abstract fun playlistsDao(): PlaylistsDao

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