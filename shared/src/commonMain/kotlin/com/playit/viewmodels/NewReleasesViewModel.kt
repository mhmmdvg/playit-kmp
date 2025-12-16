package com.playit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playit.data.remote.repository.AlbumsRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.domain.models.NewReleasesResponse
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class NewReleasesViewModel(
    private val albumsRepositoryImpl: AlbumsRepositoryImpl
) : ViewModel() {
    private val _newReleases = MutableStateFlow<Resource<NewReleasesResponse>>(Resource.Success(null))
    val newReleases: StateFlow<Resource<NewReleasesResponse>> = _newReleases.asStateFlow()
    val newReleasesFlow: CommonFlow<Resource<NewReleasesResponse>> = _newReleases.asCommonFlow()
    var fetchJob: Job? = null

    init {
        loadNewReleases()
    }

    fun loadNewReleases(forceRefresh: Boolean = false) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            if (forceRefresh) {
                albumsRepositoryImpl.invalidateCache()
            }

            albumsRepositoryImpl.getNewReleases()
                .onStart {
                    if (_newReleases.value.data == null) {
                        _newReleases.value = Resource.Loading()
                    }
                }
                .catch { exception ->
                    _newReleases.value =
                        Resource.Error(
                            message = exception.message ?: "Something went wrong",
                            data = _newReleases.value.data
                        )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { response ->
                            _newReleases.value = Resource.Success(response)
                        },
                        onFailure = { exception ->
                            _newReleases.value =
                                Resource.Error(
                                    message = exception.message ?: "Something went wrong",
                                    data = _newReleases.value.data
                                )
                        }
                    )
                }
        }
    }

    fun retry() {
        loadNewReleases(forceRefresh = true)
    }


    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}