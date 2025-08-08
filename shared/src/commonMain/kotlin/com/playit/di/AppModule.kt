package com.playit.di

import com.playit.remote.api.PlaylistsApi
import com.playit.remote.repository.AuthenticationRepository
import com.playit.remote.repository.PlaylistsRepository
import org.koin.dsl.module

val appModule = module {
    single { PlaylistsApi(get()) }
    single { PlaylistsRepository(get()) }
    single {
        AuthenticationRepository(
            tokenManager = get(),
            httpClient = get()
        )
    }
}