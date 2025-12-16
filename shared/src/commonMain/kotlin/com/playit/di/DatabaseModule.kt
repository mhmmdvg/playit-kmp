package com.playit.di

import com.playit.data.remote.local.database.AppDatabase
import com.playit.data.remote.local.database.DatabaseDriveFactory
import com.playit.data.remote.local.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

fun databaseModule(): Module = module {
    single<AppDatabase> { getDatabaseBuilder(get<DatabaseDriveFactory>()) }
    single { get<AppDatabase>().albumsDao() }
    single { get<AppDatabase>().playlistsDao() }
}