package com.playit.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playit.constants.SpotifyConfig
import com.playit.presentation.ui.screens.authentication.AuthenticationState
import com.playit.remote.repository.AuthenticationRepositoryImpl
import com.playit.remote.resources.Resource
import com.playit.utils.OAuthCallbackManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authenticationRepositoryImpl: AuthenticationRepositoryImpl
) : ViewModel() {

    private val _authUiState = MutableStateFlow(AuthenticationState())
    val authUiState: StateFlow<AuthenticationState> = _authUiState.asStateFlow()

    private var oauthTimeoutJob: Job? = null
    private var oauthInProgress = false

    init {
        checkAuthenticationStatus()
        observeOAuthCallbacks()
        observeAppLifecycle()
    }

    fun signIn(onLaunchOAuth: (String) -> Unit, onSetTabCloseListener: (() -> Unit) -> Unit) {
        _authUiState.value = _authUiState.value.copy(isLoading = true, errorMessage = null)
        oauthInProgress = true

        val oauthUrl = SpotifyConfig.AUTH_URL + SpotifyConfig.URL_PARAMS

        // Set custom tab launched flag BEFORE launching
        OAuthCallbackManager.setCustomTabLaunched()

        onSetTabCloseListener {
            if (oauthInProgress) {
                onOAuthCancelled()
            }
        }

        onLaunchOAuth(oauthUrl)

        // Start timeout to handle cancellation
        startOAuthTimeout()
    }

    fun getToken(): String? {
        return authenticationRepositoryImpl.getAccessToken()
    }

    private fun startOAuthTimeout() {
        oauthTimeoutJob?.cancel()
        oauthTimeoutJob = viewModelScope.launch {
            delay(30000)
            if (oauthInProgress) {
                onOAuthCancelled()
            }
        }
    }

    private fun observeOAuthCallbacks() {

        viewModelScope.launch {
            OAuthCallbackManager.authorizationCode.collect { code ->
                onAuthorizationCodeReceived(code)
            }
        }

        viewModelScope.launch {
            OAuthCallbackManager.authorizationError.collect { error ->
                onOAuthError(error)
            }
        }
    }

    private fun observeAppLifecycle() {
        viewModelScope.launch {
            OAuthCallbackManager.appResumed.collect {

                // If app is resumed and OAuth was in progress and a custom tab was launched,
                // wait a bit to see if we get a callback
                if (oauthInProgress && OAuthCallbackManager.isCustomTabLaunched()) {
                    delay(500) // Wait 1 second after resume to ensure no redirect is coming
                    // If still no callback after delay, consider it cancelled
                }
            }
        }
    }

    private fun onAuthorizationCodeReceived(code: String) {
        oauthInProgress = false
        oauthTimeoutJob?.cancel()
        // Reset custom tab launched state
        OAuthCallbackManager.resetCustomTabLaunched()

        viewModelScope.launch {
            authenticationRepositoryImpl.exchangeCodeForToken(code) { result ->
                when (result) {
                    is Resource.Success -> {
                        _authUiState.value = _authUiState.value.copy(
                            isLoading = true,
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

    private fun onOAuthError(error: String) {
        oauthInProgress = false
        oauthTimeoutJob?.cancel()
        // Reset custom tab launched state
        OAuthCallbackManager.resetCustomTabLaunched()

        _authUiState.value = _authUiState.value.copy(
            isLoading = false,
            errorMessage = error
        )
    }

    private fun onOAuthCancelled() {
        oauthInProgress = false
        oauthTimeoutJob?.cancel()
        // Reset custom tab launched state
        OAuthCallbackManager.resetCustomTabLaunched()

        _authUiState.value = _authUiState.value.copy(
            isLoading = false,
            errorMessage = null // No error message for cancellation
        )
    }

    fun cancelOAuth() {
        if (oauthInProgress) {
            onOAuthCancelled()
        }
    }

    fun signOut() {
        authenticationRepositoryImpl.logout()
        _authUiState.value = _authUiState.value.copy(isAuthenticated = false)
        onOAuthCancelled()
    }

    fun clearErrorMessage() {
        _authUiState.value = _authUiState.value.copy(errorMessage = null)
    }

    private fun checkAuthenticationStatus() {
        val isAuthenticated = authenticationRepositoryImpl.isUserLoggedIn()
        _authUiState.value = _authUiState.value.copy(isAuthenticated = isAuthenticated)
    }

    override fun onCleared() {
        super.onCleared()
        oauthTimeoutJob?.cancel()
        // Reset custom tab launched state when ViewModel is cleared
        OAuthCallbackManager.resetCustomTabLaunched()
    }
}