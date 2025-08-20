package com.playit.di

import com.playit.remote.api.AlbumsApi
import com.playit.remote.api.PlaylistsApi
import com.playit.remote.api.TracksApi
import com.playit.remote.repository.AlbumsRepository
import com.playit.remote.repository.AuthenticationRepository
import com.playit.remote.repository.PlaylistsRepository
import com.playit.remote.repository.TracksRepository
import com.playit.viewmodels.CurrentPlaylistsViewModel
import com.playit.viewmodels.NewReleasesViewModel
import com.playit.viewmodels.TracksViewModel
import org.koin.dsl.module

val appModule = module {
    single {
        AuthenticationRepository(
            tokenManager = get(),
            httpClient = get()
        )
    }

//    Playlists DI
    single { PlaylistsApi(get()) }
    single { PlaylistsRepository(get()) }
    single { CurrentPlaylistsViewModel(get()) }


//    Albums DI
    single { AlbumsApi(get()) }
    single { AlbumsRepository(get()) }
    single { NewReleasesViewModel(get()) }

//    Tracks DI
    single { TracksApi(get()) }
    single { TracksRepository(get()) }
    single { TracksViewModel(get()) }
}