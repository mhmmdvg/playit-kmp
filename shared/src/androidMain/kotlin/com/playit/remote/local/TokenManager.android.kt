package com.playit.remote.local

import android.content.SharedPreferences
import android.util.Base64
import androidx.core.content.edit
import org.json.JSONObject
import java.nio.charset.StandardCharsets

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TokenManager(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val TOKEN_KEY = "access_token"
    }

    actual fun saveToken(token: String) {
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
            val parts = token.split(".")

            if (parts.size != 3) return true

            val payload = parts[1]
            val decodedPayload = String(Base64.decode(payload, Base64.URL_SAFE), StandardCharsets.UTF_8)

            val jsonObject = JSONObject(decodedPayload)
            val expirationTime = jsonObject.optLong("exp", 0)

            if (expirationTime == 0L) return true

            val currentTime = System.currentTimeMillis() / 1000
            expirationTime < currentTime
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }

    actual fun clearToken() {
        sharedPreferences.edit { remove(TOKEN_KEY) }
    }
}