package com.playit.di

import com.playit.network.provideHttpClient
import org.koin.dsl.module

val networkModule = module {
    single { provideHttpClient(get()) }
}