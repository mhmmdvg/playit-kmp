package com.playit.di

import com.playit.network.provideHttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val networkModule = module {
    singleOf(::provideHttpClient)
}