package com.playit.remote.local

import platform.Foundation.NSDate
import platform.Foundation.NSUserDefaults
import platform.Foundation.timeIntervalSince1970

actual class TokenManager {

    companion object {
        private const val TOKEN_KEY = "access_token"
        private const val TOKEN_EXPIRES_KEY = "token_expires_at"
    }

    actual fun saveToken(token: String) {
        NSUserDefaults.standardUserDefaults.setObject(token, TOKEN_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }

    actual fun getToken(): String? {
        val token = NSUserDefaults.standardUserDefaults.stringForKey(TOKEN_KEY)

        if (token.isNullOrEmpty()) return null

        if (isTokenExpired(token)) {
            clearToken()
            return null
        }

        return token
    }

    actual fun isTokenExpired(token: String): Boolean {
        return try {
            val expirationTime = NSUserDefaults.standardUserDefaults.doubleForKey(TOKEN_EXPIRES_KEY)

            if (expirationTime == 0.0) {
                return false
            }

            val currentTime = NSDate().timeIntervalSince1970
            val isExpired = currentTime >= expirationTime

            isExpired
        } catch (error: Exception) {
            error.printStackTrace()
            false
        }
    }

    actual fun clearToken() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(TOKEN_KEY)
        NSUserDefaults.standardUserDefaults.removeObjectForKey(TOKEN_EXPIRES_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }
}
