package com.playit.remote.repository

import com.playit.constants.SpotifyConfig
import com.playit.domain.models.AuthenticationError
import com.playit.domain.models.AuthenticationResponse
import com.playit.remote.local.TokenManager
import com.playit.remote.resources.Resource
import com.playit.utils.CommonFlow
import com.playit.utils.asCommonFlow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationRepository(
    private val tokenManager: TokenManager,
    private val httpClient: HttpClient
) {
    private val _authStateFlow = MutableStateFlow(isUserLoggedIn())
    val authStateFlow: StateFlow<Boolean> = _authStateFlow.asStateFlow()
    val authStateCommonFlow: CommonFlow<Boolean> = _authStateFlow.asCommonFlow()

    fun isUserLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }

//    fun validateAndUpdateAuthState() {
//        val currentAuthState = isUserLoggedIn()
//
//        if(_authStateFlow.value != currentAuthState) {
//            _authStateFlow.value = currentAuthState
//        }
//    }

    fun exchangeCodeForToken(
        code: String,
        completion: (Resource<Boolean>) -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = httpClient.submitForm(
                    url = SpotifyConfig.TOKEN_URL,
                    formParameters = parameters {
                        append("grant_type", "authorization_code")
                        append("code", code)
                        append("redirect_uri", SpotifyConfig.REDIRECT_URI)
                        append("client_id", SpotifyConfig.CLIENT_ID)
                        append("client_secret", SpotifyConfig.CLIENT_SECRET)
                    }
                )

                when (response.status) {
                    HttpStatusCode.OK -> {
                        val tokenResponse = response.body<AuthenticationResponse>()
                        tokenManager.saveToken(tokenResponse.accessToken)
                        _authStateFlow.value = true
                        completion(Resource.Success(true))
                    }
                    else -> {
                        val errorResponse = response.body<AuthenticationError>()
                        completion(Resource.Error(errorResponse.errorDescription))
                    }
                }
            } catch (error: Exception) {
                completion(Resource.Error(error.message ?: "Unknown error occurred"))
            }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _authStateFlow.value = false
    }

    fun getAccessToken(): String? {
        return tokenManager.getToken()
    }

    fun refreshAuthState() {
        _authStateFlow.value = isUserLoggedIn()
    }
}