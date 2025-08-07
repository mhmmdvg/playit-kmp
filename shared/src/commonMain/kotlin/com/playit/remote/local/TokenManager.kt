package com.playit.remote.local

expect class TokenManager {
    fun saveToken(token: String)
    fun getToken(): String?
    fun isTokenExpired(token: String): Boolean
    fun clearToken()
}