package com.playit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playit.presentation.ui.screens.authentication.AuthenticationState
import com.playit.remote.repository.AuthenticationRepository
import com.playit.remote.resources.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticaitonViewModel(
    private val authenticationRepository: AuthenticationRepository
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthenticationState())
    val authUiState: StateFlow<AuthenticationState> = _authUiState.asStateFlow()

    private val _showWebView = MutableStateFlow(false)
    val showWebView: StateFlow<Boolean> = _showWebView.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    fun signIn() {
        _authUiState.value = _authUiState.value.copy(isLoading = true, errorMessage = null)
        _showWebView.value = true
    }

    fun onAuthorizationCodeReceived(code: String) {
        _showWebView.value = false

        viewModelScope.launch {
            authenticationRepository.exchangeCodeForToken(code) { result ->
                when (result) {
                    is Resource.Success -> {
                        _authUiState.value = _authUiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            errorMessage = null
                        )
                    }
                    is Resource.Error -> {
                        _authUiState.value = _authUiState.value.copy(
                            isLoading = false,
                            isAuthenticated = false,
                            errorMessage = result.message
                        )
                    }
                    is Resource.Loading -> {
                        _authUiState.value = _authUiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun onWebViewError(error: String) {
        _showWebView.value = false
        _authUiState.value = _authUiState.value.copy(
            isLoading = false,
            errorMessage = error
        )
    }

    fun onWebViewCancelled() {
        _showWebView.value = false
        _authUiState.value = _authUiState.value.copy(isLoading = false)
    }

    fun signOut() {
        authenticationRepository.logout()
        _authUiState.value = _authUiState.value.copy(isAuthenticated = false)
    }

    fun clearErrorMessage() {
        _authUiState.value = _authUiState.value.copy(errorMessage = null)
    }

    private fun checkAuthenticationStatus() {
        val isAuthenticated = authenticationRepository.isUserLoggedIn()
        _authUiState.value = _authUiState.value.copy(isAuthenticated = isAuthenticated)
    }
}