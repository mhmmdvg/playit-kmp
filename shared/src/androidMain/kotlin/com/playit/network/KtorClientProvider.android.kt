package com.playit.network

import com.playit.remote.local.TokenManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
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

        defaultRequest {
            url("https://api.spotify.com/v1")
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            tokenManager.getToken().let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
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