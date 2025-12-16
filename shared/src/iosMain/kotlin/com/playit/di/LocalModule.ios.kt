package com.playit.di

import com.playit.data.remote.local.TokenManager
import com.playit.data.remote.local.database.DatabaseDriveFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun localModule(): Module = module {
    singleOf(::TokenManager)
    singleOf(::DatabaseDriveFactory)
}