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
//    API
    single { PlaylistsApi(get()) }
    single { AlbumsApi(get()) }
    single { TracksApi(get()) }

//    Repository
    single {
        AuthenticationRepository(
            tokenManager = get(),
            httpClient = get()
        )
    }
    single { PlaylistsRepository(get()) }
    single { AlbumsRepository(get()) }
    single { TracksRepository(get()) }


//    View Models
    factory { CurrentPlaylistsViewModel(get()) }
    factory<NewReleasesViewModel> { NewReleasesViewModel(get()) }
    factory { TracksViewModel(get()) }
}