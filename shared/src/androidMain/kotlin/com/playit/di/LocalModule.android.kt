package com.playit.di

import android.app.Application
import android.content.SharedPreferences
import com.playit.data.remote.local.TokenManager
import com.playit.data.remote.local.database.DatabaseDriveFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun localModule(): Module = module {
    single<SharedPreferences> { get<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE) }
    singleOf(::TokenManager)
    single { DatabaseDriveFactory(androidContext()) }
}