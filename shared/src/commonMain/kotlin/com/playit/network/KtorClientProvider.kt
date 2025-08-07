package com.playit.network

import com.playit.remote.local.TokenManager
import io.ktor.client.HttpClient

expect fun provideHttpClient(tokenManager: TokenManager): HttpClient