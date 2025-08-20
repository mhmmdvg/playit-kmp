package com.playit.remote.local

import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.core.content.edit
import org.json.JSONObject
import java.nio.charset.StandardCharsets

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TokenManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TOKEN_KEY = "access_token"
        private const val TOKEN_EXPIRES_KEY = "expires_in"
    }

    actual fun saveToken(token: String, expiresIn: Int) {
        sharedPreferences.edit { putString(TOKEN_KEY, token) }
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

            if (expirationTime == 0.0) {
                return false
            }

            val currentTime = System.currentTimeMillis() / 1000.0
            val isExpired = currentTime >= expirationTime

            isExpired
        } catch (e: Exception) {
            false
        }
    }

    actual fun clearToken() {
        sharedPreferences.edit { remove(TOKEN_KEY) }
    }
}