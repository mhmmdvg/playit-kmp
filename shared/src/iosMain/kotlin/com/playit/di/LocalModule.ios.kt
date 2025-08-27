package com.playit.di

import com.playit.remote.local.TokenManager
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun localModule(): Module = module {
    singleOf(::TokenManager)
}