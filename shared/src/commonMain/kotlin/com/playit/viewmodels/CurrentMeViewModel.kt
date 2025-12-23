package com.playit.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playit.data.remote.repository.ProfileRepositoryImpl
import com.playit.data.remote.resources.Resource
import com.playit.domain.models.ProfileResponse
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CurrentMeViewModel(
    private val profileRepositoryImpl: ProfileRepositoryImpl,
) : ViewModel() {
    private val _currentMe = MutableStateFlow<Resource<ProfileResponse>>(Resource.Success(null))
    val currentMe: StateFlow<Resource<ProfileResponse>> = _currentMe.asStateFlow()
    val currentMeFlow: CommonFlow<Resource<ProfileResponse>> = _currentMe.asCommonFlow()

    var fetchJob: Job? = null

    init {
        loadCurrentMe()
    }

    fun loadCurrentMe() {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            profileRepositoryImpl.getCurrentProfile()
                .onStart {
                    if (_currentMe.value.data == null) {
                        _currentMe.emit(Resource.Loading())
                    }
                }
                .catch { exception ->
                    _currentMe.emit(
                        Resource.Error(
                            message = exception.message ?: "Something went wrong",
                            data = _currentMe.value.data
                        )
                    )
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { response ->
                            _currentMe.emit(Resource.Success(response))
                        },
                        onFailure = { exception ->
                            _currentMe.emit(
                                Resource.Error(
                                    message = exception.message ?: "Something went wrong",
                                    data = _currentMe.value.data
                                )
                            )
                        }
                    )
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        fetchJob?.cancel()
    }
}