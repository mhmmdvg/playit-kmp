package com.playit.remote.repository

import com.playit.constants.SpotifyConfig
import com.playit.domain.models.AuthenticationError
import com.playit.domain.models.AuthenticationResponse
import com.playit.remote.local.TokenManager
import com.playit.remote.resources.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HttpStatusCode
import io.ktor.http.parameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthenticationRepository(
    private val tokenManager: TokenManager,
    private val httpClient: HttpClient
) {

    fun isUserLoggedIn(): Boolean {
        return tokenManager.getToken() != null
    }

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
    }

    fun getAccessToken(): String? {
        return tokenManager.getToken()
    }

}