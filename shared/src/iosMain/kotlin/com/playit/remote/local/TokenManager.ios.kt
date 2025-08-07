package com.playit.remote.local

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
import platform.Foundation.NSData
import platform.Foundation.NSDataBase64DecodingIgnoreUnknownCharacters
import platform.Foundation.NSDate
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDefaults
import platform.Foundation.create
import platform.Foundation.timeIntervalSince1970

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TokenManager {

    companion object {
        private const val TOKEN_KEY = "access_token"
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
            val parts = token.split(".")

            if (parts.size != 3) return true

            val payload = parts[1]

            // Base64 decoding for iOS
            val data = NSData.create(
                base64EncodedString = payload.replace('-', '+').replace('_', '/'),
                options = NSDataBase64DecodingIgnoreUnknownCharacters
            ) ?: return true

            val decodedString = NSString.create(data = data, encoding = NSUTF8StringEncoding)?.toString()
                ?: return true

            val json = Json.parseToJsonElement(decodedString).jsonObject
            val expirationTime = json["exp"]?.jsonPrimitive?.longOrNull ?: return true

            val currentTime = NSDate().timeIntervalSince1970.toLong()
            expirationTime < currentTime
        } catch (e: Exception) {
            println("Token validation error: ${e.message}")
            true
        }
    }

    actual fun clearToken() {
        NSUserDefaults.standardUserDefaults.removeObjectForKey(TOKEN_KEY)
        NSUserDefaults.standardUserDefaults.synchronize()
    }
}
