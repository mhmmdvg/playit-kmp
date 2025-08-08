package com.playit.network

import com.playit.remote.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun provideHttpClient(tokenManager: TokenManager): HttpClient {
    return HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(Logging) {
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = tokenManager.getToken()
                    token?.let {
                        BearerTokens(accessToken = it, refreshToken = null)
                    }
                }

                refreshTokens {
                    val token = tokenManager.getToken()
                    token?.let {
                        BearerTokens(accessToken = it, refreshToken = null)
                    }
                }
            }
        }

        defaultRequest {
            url("https://api.spotify.com/v1")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    throw ResponseException(response, "Error ${response.status}")
                }
            }

            handleResponseExceptionWithRequest { exception, _ ->
                throw exception
            }
        }
    }
}