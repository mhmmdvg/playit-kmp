package com.playit.di

import com.playit.remote.repository.AlbumsRepository
import com.playit.remote.repository.AuthenticationRepository
import com.playit.remote.repository.PlaylistsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    private val playlistsRepository: PlaylistsRepository by inject()
    private val authenticationRepository: AuthenticationRepository by inject()

    private val albumsRepository: AlbumsRepository by inject()

    fun providePlaylistsRepository(): PlaylistsRepository = playlistsRepository
    fun provideAuthenticationRepository(): AuthenticationRepository = authenticationRepository
    fun provdeAlbumsRepository(): AlbumsRepository = albumsRepository

    companion object {
        val shared = KoinHelper()
    }
}