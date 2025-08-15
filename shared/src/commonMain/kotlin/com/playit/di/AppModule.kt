package com.playit.di

import com.playit.remote.api.AlbumsApi
import com.playit.remote.api.PlaylistsApi
import com.playit.remote.repository.AlbumsRepository
import com.playit.remote.repository.AuthenticationRepository
import com.playit.remote.repository.PlaylistsRepository
import com.playit.viewmodels.NewReleasesViewModel
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

    single { AlbumsApi(get()) }
    single { AlbumsRepository(get()) }
    single { NewReleasesViewModel(get()) }
}