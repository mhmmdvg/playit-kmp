package com.playit.di

import com.playit.data.cache.DataStoreFactory
import com.playit.data.cache.NewReleasesCacheStore
import com.playit.domain.repository.AlbumsRepository
import com.playit.domain.repository.AuthenticationRepository
import com.playit.domain.repository.PlaylistsRepository
import com.playit.domain.repository.TracksRepository
import com.playit.data.remote.api.AlbumsApi
import com.playit.data.remote.api.PlaylistsApi
import com.playit.data.remote.api.TracksApi
import com.playit.data.remote.repository.AlbumsRepositoryImpl
import com.playit.data.remote.repository.AuthenticationRepositoryImpl
import com.playit.data.remote.repository.PlaylistsRepositoryImpl
import com.playit.data.remote.repository.TracksRepositoryImpl
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

    // Cache
    single { DataStoreFactory.createDataStore() }
    singleOf(::NewReleasesCacheStore)

//    Repository
    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()
    singleOf(::PlaylistsRepositoryImpl).bind<PlaylistsRepository>()
    singleOf(::AlbumsRepositoryImpl).bind<AlbumsRepository>()
    singleOf(::TracksRepositoryImpl).bind<TracksRepository>()


//    View Models
    factoryOf(::CurrentPlaylistsViewModel)
    factoryOf(::NewReleasesViewModel)
    factoryOf(::TracksViewModel)
}