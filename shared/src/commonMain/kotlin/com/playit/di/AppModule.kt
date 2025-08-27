package com.playit.di

import com.playit.domain.repository.AuthenticationRepository
import com.playit.remote.api.AlbumsApi
import com.playit.remote.api.PlaylistsApi
import com.playit.remote.api.TracksApi
import com.playit.remote.repository.AlbumsRepositoryImpl
import com.playit.remote.repository.AuthenticationRepositoryImpl
import com.playit.remote.repository.PlaylistsRepositoryImpl
import com.playit.remote.repository.TracksRepositoryImpl
import com.playit.viewmodels.CurrentPlaylistsViewModel
import com.playit.viewmodels.NewReleasesViewModel
import com.playit.viewmodels.TracksViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
//    API
    singleOf(::PlaylistsApi)
    singleOf(::AlbumsApi)
    singleOf(::TracksApi)

//    Repository
    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()
    singleOf(::PlaylistsRepositoryImpl).bind<PlaylistsRepositoryImpl>()
    singleOf(::AlbumsRepositoryImpl).bind<AlbumsRepositoryImpl>()
    singleOf(::TracksRepositoryImpl).bind<TracksRepositoryImpl>()


//    View Models
    factoryOf(::CurrentPlaylistsViewModel)
    factoryOf(::NewReleasesViewModel)
    factoryOf(::TracksViewModel)
}