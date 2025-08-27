package com.playit.viewmodels

import com.playit.domain.models.NewReleasesResponse
import com.playit.remote.repository.AlbumsRepositoryImpl
import com.playit.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewReleasesViewModel(
    private val albumsRepositoryImpl: AlbumsRepositoryImpl
) : BaseViewModel() {
    private val _newReleases = MutableStateFlow<Resource<NewReleasesResponse>>(Resource.Loading())
    val newReleases: StateFlow<Resource<NewReleasesResponse>> = _newReleases.asStateFlow()

    val newReleasesFlow: CommonFlow<Resource<NewReleasesResponse>> = _newReleases.asCommonFlow()

    init {
        getNewReleases()
    }

    fun getNewReleases() {
        viewModelScope.launch {
            _newReleases.value = Resource.Loading()

            try {
                albumsRepositoryImpl.getNewReleases().fold(
                    onSuccess = { success ->
                        _newReleases.value = Resource.Success(success)
                    },
                    onFailure = { error ->
                        _newReleases.value = Resource.Error(error.message ?: "Something went wrong")
                    }
                )
            } catch (error: Exception) {
                _newReleases.value = Resource.Error(error.message ?: "Something went wrong")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}