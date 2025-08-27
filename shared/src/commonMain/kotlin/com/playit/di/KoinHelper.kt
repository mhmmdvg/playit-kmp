package com.playit.di

import com.playit.remote.repository.AlbumsRepositoryImpl
import com.playit.remote.repository.AuthenticationRepositoryImpl
import com.playit.remote.repository.PlaylistsRepositoryImpl
import com.playit.remote.repository.TracksRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    private val playlistsRepositoryImpl: PlaylistsRepositoryImpl by inject()
    private val authenticationRepositoryImpl: AuthenticationRepositoryImpl by inject()
    private val albumsRepositoryImpl: AlbumsRepositoryImpl by inject()
    private val tracksRepositoryImpl: TracksRepositoryImpl by inject()

    fun providePlaylistsRepository(): PlaylistsRepositoryImpl = playlistsRepositoryImpl
    fun provideAuthenticationRepository(): AuthenticationRepositoryImpl = authenticationRepositoryImpl
    fun provideAlbumsRepository(): AlbumsRepositoryImpl = albumsRepositoryImpl
    fun provideTracksRepository(): TracksRepositoryImpl = tracksRepositoryImpl

    companion object {
        val shared = KoinHelper()
    }
}