package com.playit.viewmodels

import com.playit.domain.models.NewReleasesResponse
import com.playit.data.remote.repository.AlbumsRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class NewReleasesViewModel(
    private val albumsRepositoryImpl: AlbumsRepositoryImpl
) : BaseViewModel() {
    private val _newReleases = MutableStateFlow(initializeWithCache())
    val newReleases: StateFlow<Resource<NewReleasesResponse>> = _newReleases.asStateFlow()

    val newReleasesFlow: CommonFlow<Resource<NewReleasesResponse>> = _newReleases.asCommonFlow()

    init {
        if (_newReleases.value is Resource.Loading) {
            getNewReleases()
        }
    }

    private fun initializeWithCache(): Resource<NewReleasesResponse> {
        return try {
            val cachedData = runBlocking { albumsRepositoryImpl.getCachedData() }
            if (cachedData != null && albumsRepositoryImpl.cacheIsValid(cachedData.timestamp)) {
                Resource.Success(cachedData.data)
            } else {
                Resource.Loading()
            }
        } catch (error: Exception) {
            println("Cache loading failed during initialization: ${error.message}")
            Resource.Loading()
        }
    }
    fun getNewReleases() {
        viewModelScope.launch {
            if (_newReleases.value !is Resource.Success) {
                _newReleases.value = Resource.Loading()
            }

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
}