package com.playit.di

import com.playit.remote.repository.PlaylistsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KoinHelper : KoinComponent {
    private val playlistsRepository: PlaylistsRepository by inject()

    fun providePlaylistsRepository(): PlaylistsRepository = playlistsRepository

    companion object {
        val shared = KoinHelper()
    }
}