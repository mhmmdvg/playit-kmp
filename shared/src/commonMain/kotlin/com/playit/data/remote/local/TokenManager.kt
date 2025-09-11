package com.playit.data.remote.local

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class TokenManager {
    fun saveToken(token: String, expiresIn: Long = 3600)
    fun getToken(): String?
    fun isTokenExpired(token: String): Boolean
    fun clearToken()
}