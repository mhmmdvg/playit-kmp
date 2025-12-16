package com.playit.data.remote.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import platform.Foundation.NSHomeDirectory

actual class DatabaseDriveFactory {
    actual fun createDriver(): RoomDatabase.Builder<AppDatabase> {
        val dbFilePath = "${NSHomeDirectory()}/playit_database.db"

        return Room.databaseBuilder<AppDatabase>(
            name = dbFilePath
        )
    }
}
