package com.playit.remote.local

import platform.Foundation.NSDate
import platform.Foundation.NSUserDefaults
import platform.Foundation.timeIntervalSince1970

actual class TokenManager {

    companion object {
        private const val TOKEN_KEY = "access_token"
        private const val TOKEN_EXPIRES_KEY = "expires_in"
    }

    actual fun saveToken(token: String, expiresIn: Long) {
        NSUserDefaults.standardUserDefaults.setObject(token, TOKEN_KEY)

        val currentTime = NSDate().timeIntervalSince1970
        val expirationTime = currentTime + expiresIn

        NSUserDefaults.standardUserDefaults.setDouble(expirationTime, TOKEN_EXPIRES_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }

    actual fun getToken(): String? {
        val token = NSUserDefaults.standardUserDefaults.stringForKey(TOKEN_KEY)

        if (token.isNullOrEmpty()) return null

        if (isTokenExpired(token)) {
            return null
        }

        return token
    }

    actual fun isTokenExpired(token: String): Boolean {
        return try {
            val expirationTime = NSUserDefaults.standardUserDefaults.doubleForKey(TOKEN_EXPIRES_KEY)

            val currentTime = NSDate().timeIntervalSince1970
            val isExpired = currentTime >= expirationTime

            isExpired
        } catch (error: Exception) {
            error.printStackTrace()
            true
        }
    }

    actual fun clearToken() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(TOKEN_KEY)
        NSUserDefaults.standardUserDefaults.removeObjectForKey(TOKEN_EXPIRES_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }
}
