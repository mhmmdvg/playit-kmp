package com.playit.di

import android.app.Application
import android.content.SharedPreferences
import com.playit.remote.local.TokenManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun localModule(): Module = module {
    single<SharedPreferences> { get<Application>().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE) }
    single<TokenManager> { TokenManager(get()) }
}