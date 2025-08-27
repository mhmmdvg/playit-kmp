package com.playit.viewmodels

import com.playit.domain.models.SeveralTracks
import com.playit.remote.repository.TracksRepositoryImpl
import com.playit.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TracksViewModel(
    private val tracksRepositoryImpl: TracksRepositoryImpl
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _severalTracks = MutableStateFlow<Resource<SeveralTracks>>(Resource.Success(null))
    val severalTracks: StateFlow<Resource<SeveralTracks>> = _severalTracks.asStateFlow()
    val severalTracksFlow: CommonFlow<Resource<SeveralTracks>> = _severalTracks.asCommonFlow()

    fun getSeveralTracks() {
        viewModelScope.launch {
            _severalTracks.value = Resource.Loading()

            try {
                tracksRepositoryImpl.getSeveralTracks().fold(
                    onSuccess = { success ->
                        _severalTracks.value = Resource.Success(success)
                    },
                    onFailure = { failure ->
                        _severalTracks.value = Resource.Error(failure.message ?: "Something went wrong")
                    }
                )
            } catch (error: Exception) {
                _severalTracks.value = Resource.Error(error.message ?: "Something went wrong")
            }
        }
    }
}