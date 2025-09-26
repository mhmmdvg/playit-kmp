package com.playit.viewmodels

import com.playit.data.remote.repository.ProfileRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.domain.models.ProfileResponse
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CurrentMeViewModel(
    private val profileRepositoryImpl: ProfileRepositoryImpl,
) : BaseViewModel() {
    private val _currentMe = MutableStateFlow(initializeWithCache())
    val currentMe: StateFlow<Resource<ProfileResponse>> = _currentMe.asStateFlow()
    val currentMeFlow: CommonFlow<Resource<ProfileResponse>> = _currentMe.asCommonFlow()

    init {
        if (_currentMe.value is Resource.Loading) {
            getCurrentMe()
        }
    }

    private fun initializeWithCache(): Resource<ProfileResponse> {
        return try {
            val cachedData = runBlocking(Dispatchers.IO) { profileRepositoryImpl.getCachedData() }
            if (cachedData != null && profileRepositoryImpl.cacheIsValid(cachedData.timestamp)) {
                Resource.Success(cachedData.data)
            } else {
                Resource.Loading()
            }
        } catch (error: Exception) {
            println("Cache loading failed during initialization: ${error.message}")
            Resource.Loading()
        }
    }

    fun getCurrentMe() {
        viewModelScope.launch {
            if (_currentMe.value !is Resource.Success) {
                _currentMe.value = Resource.Loading()
            }

            try {
                profileRepositoryImpl.getCurrentProfile().fold(
                    onSuccess = { _currentMe.value = Resource.Success(it) },
                    onFailure = { _currentMe.value = Resource.Error(it.message ?: "Something went wrong") }
                )
            } catch (error: Exception) {
                _currentMe.value = Resource.Error(error.message ?: "Something went wrong")
            }
        }
    }
}