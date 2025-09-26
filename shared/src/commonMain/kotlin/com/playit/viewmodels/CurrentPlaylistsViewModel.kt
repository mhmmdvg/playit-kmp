package com.playit.viewmodels

import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.data.remote.repository.PlaylistsRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentPlaylistsViewModel(
    private val playlistsRepositoryImpl: PlaylistsRepositoryImpl
) : BaseViewModel() {
    private val _currentPlaylists = MutableStateFlow<Resource<CurrentPlaylistsResponse>>(initializeWithCache())
    val currentPlaylists: StateFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asStateFlow()
    val currentPlaylistsFlow: CommonFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asCommonFlow()

    init {
        if (_currentPlaylists.value is Resource.Loading) {
            getCurrentPlaylists()
        }
    }

    private fun initializeWithCache(): Resource<CurrentPlaylistsResponse> {
        return try {
            val cachedData = runBlocking(Dispatchers.IO) { playlistsRepositoryImpl.getCachedData() }
            if (cachedData != null && playlistsRepositoryImpl.cacheIsValid(cachedData.timestamp)) {
                Resource.Success(cachedData.data)
            } else {
                Resource.Loading()
            }
        } catch (error: Exception) {
            println("Cache loading failed during initialization: ${error.message}")
            Resource.Loading()
        }
    }

    fun getCurrentPlaylists() {
        viewModelScope.launch {
            if (_currentPlaylists.value !is Resource.Success) {
                _currentPlaylists.value = Resource.Loading()
            }

            try {
                playlistsRepositoryImpl.getCurrentPlaylists().fold(
                    onSuccess = {
                        _currentPlaylists.value = Resource.Success(it)
                    },
                    onFailure = {
                        _currentPlaylists.value = Resource.Error(it.message ?: "Something went wrong")
                    }
                )
            } catch (e: Exception) {
                _currentPlaylists.value = Resource.Error(e.message ?: "Something went wrong")
            }
        }
    }

}