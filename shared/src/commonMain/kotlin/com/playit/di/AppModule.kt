package com.playit.di

import com.playit.data.cache.DataStoreFactory
import com.playit.data.cache.NewReleasesCacheStore
import com.playit.data.cache.PlaylistCacheStore
import com.playit.data.cache.ProfileCacheStore
import com.playit.data.remote.api.AlbumsApi
import com.playit.data.remote.api.PlaylistsApi
import com.playit.data.remote.api.ProfileApi
import com.playit.data.remote.api.TracksApi
import com.playit.data.remote.repository.*
import com.playit.domain.repository.*
import com.playit.viewmodels.CurrentMeViewModel
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
    singleOf(::ProfileApi)

    // Cache
    single { DataStoreFactory.createDataStore() }
    singleOf(::NewReleasesCacheStore)
    singleOf(::PlaylistCacheStore)
    singleOf(::ProfileCacheStore)

//    Repository
    singleOf(::AuthenticationRepositoryImpl).bind<AuthenticationRepository>()
    singleOf(::PlaylistsRepositoryImpl).bind<PlaylistsRepository>()
    singleOf(::AlbumsRepositoryImpl).bind<AlbumsRepository>()
    singleOf(::TracksRepositoryImpl).bind<TracksRepository>()
    singleOf(::ProfileRepositoryImpl).bind<ProfileRepository>()

//    View Models
    factoryOf(::CurrentPlaylistsViewModel)
    factoryOf(::NewReleasesViewModel)
    factoryOf(::TracksViewModel)
    factoryOf(::CurrentMeViewModel)
}