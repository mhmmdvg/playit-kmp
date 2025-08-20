package com.playit.remote.local

expect class TokenManager {
    fun saveToken(token: String, expiresIn: Long = 3600)
    fun getToken(): String?
    fun isTokenExpired(token: String): Boolean
    fun clearToken()
}