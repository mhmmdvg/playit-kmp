package com.playit.network

import com.playit.data.remote.local.TokenManager
import io.ktor.client.HttpClient

expect fun provideHttpClient(tokenManager: TokenManager): HttpClient