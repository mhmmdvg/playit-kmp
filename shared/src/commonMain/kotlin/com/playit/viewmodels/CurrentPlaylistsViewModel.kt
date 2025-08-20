package com.playit.viewmodels

import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.remote.repository.PlaylistsRepository
import com.playit.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CurrentPlaylistsViewModel(
    private val playlistsRepository: PlaylistsRepository
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _currentPlaylists = MutableStateFlow<Resource<CurrentPlaylistsResponse>>(Resource.Success(null))
    val currentPlaylists: StateFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asStateFlow()
    val currentPlaylistsFlow: CommonFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asCommonFlow()

    fun getCurrentPlaylists() {
        viewModelScope.launch {
            _currentPlaylists.value = Resource.Loading()

            try {
                playlistsRepository.getCurrentPlaylists().fold(
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