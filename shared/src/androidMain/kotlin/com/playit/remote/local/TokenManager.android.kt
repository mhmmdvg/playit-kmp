package com.playit.remote.local

import android.content.SharedPreferences
import androidx.core.content.edit

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TokenManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TOKEN_KEY = "access_token"
        private const val TOKEN_EXPIRES_KEY = "expires_in"
    }

    actual fun saveToken(token: String, expiresIn: Long) {
        val currentTime = System.currentTimeMillis() / 1000.0

        sharedPreferences.edit { putString(TOKEN_KEY, token) }
        sharedPreferences.edit { putFloat(TOKEN_EXPIRES_KEY, expiresIn.toFloat() + currentTime.toFloat()) }
    }

    actual fun getToken(): String? {
        val token = sharedPreferences.getString(TOKEN_KEY, "")

        if (token.isNullOrEmpty()) return null

        if (isTokenExpired(token)) {
            clearToken()
            return null
        }

        return token
    }

    actual fun isTokenExpired(token: String): Boolean {
        return try {
            val expirationTime = sharedPreferences.getFloat(TOKEN_EXPIRES_KEY, 0.0f).toDouble()

            val currentTime = System.currentTimeMillis() / 1000.0
            val isExpired = currentTime >= expirationTime

            isExpired
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    actual fun clearToken() {
        sharedPreferences.edit { remove(TOKEN_KEY) }
    }
}