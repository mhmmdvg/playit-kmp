package com.playit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playit.data.remote.repository.PlaylistsRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.domain.models.CreatePlaylistRequest
import com.playit.domain.models.CurrentPlaylistsResponse
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.fold

class CurrentPlaylistsViewModel(
    private val playlistsRepositoryImpl: PlaylistsRepositoryImpl
) : ViewModel() {
    private val _currentPlaylists = MutableStateFlow<Resource<CurrentPlaylistsResponse>>(Resource.Success(null))
    val currentPlaylists: StateFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asStateFlow()
    val currentPlaylistsFlow: CommonFlow<Resource<CurrentPlaylistsResponse>> = _currentPlaylists.asCommonFlow()

    private val _createPlaylistState = MutableStateFlow<Resource<Unit>>(Resource.Success(null))
    val createPlaylistState: StateFlow<Resource<Unit>> = _createPlaylistState.asStateFlow()
    val createPlaylistStateFlow: CommonFlow<Resource<Unit>> = _createPlaylistState.asCommonFlow()

    var fetchJob: Job? = null

    init {
        loadCurrentPlaylists()
    }

    fun loadCurrentPlaylists(forceRefresh: Boolean = false) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            if (forceRefresh) {
                playlistsRepositoryImpl.invalidateCache()
            }

            playlistsRepositoryImpl.getCurrentPlaylists()
                .onStart {
                    if (_currentPlaylists.value.data == null) {
                        _currentPlaylists.value = Resource.Loading()
                    }
                }
                .catch { exception ->
                    _currentPlaylists.value =
                        Resource.Error(
                            message = exception.message ?: "Something went wrong",
                            data = _currentPlaylists.value.data
                        )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { response ->
                            _currentPlaylists.value = Resource.Success(response)
                        },
                        onFailure = { exception ->
                            _currentPlaylists.value =
                                Resource.Error(
                                    message = exception.message ?: "Something went wrong",
                                    data = _currentPlaylists.value.data
                                )
                        }
                    )
                }
        }
    }

    fun createPlaylist(userId: String, request: CreatePlaylistRequest) {
       viewModelScope.launch {
           val result = playlistsRepositoryImpl.createPlaylist(userId, request)

           result.fold(
               onSuccess = {
                   _createPlaylistState.value = Resource.Success(Unit)
               },
               onFailure = { exception ->
                     _createPlaylistState.value =
                          Resource.Error(
                            message = exception.message ?: "Something went wrong"
                          )
               }
           )
       }
    }

    fun retry() {
        loadCurrentPlaylists(forceRefresh = true)
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}