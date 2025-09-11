package com.playit.domain.repository

import com.playit.data.remote.resources.Resource

interface AuthenticationRepository {
    fun isUserLoggedIn(): Boolean
    fun exchangeCodeForToken(code: String, completion: (Resource<Boolean>) -> Unit)
    fun logout()
    fun getAccessToken(): String?
    fun refreshAuthState()
}