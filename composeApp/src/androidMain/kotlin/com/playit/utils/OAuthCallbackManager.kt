package com.playit.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object OAuthCallbackManager {
    private val _authorizationCode = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1)
    val authorizationCode: SharedFlow<String> = _authorizationCode.asSharedFlow()

    private val _authorizationError = MutableSharedFlow<String>(replay = 0)
    val authorizationError: SharedFlow<String> = _authorizationError.asSharedFlow()

    private val _appResumed = MutableSharedFlow<Unit>(replay = 0)
    val appResumed: SharedFlow<Unit> = _appResumed.asSharedFlow()

    private val _appPaused = MutableSharedFlow<Unit>(replay = 0)
    val appPaused: SharedFlow<Unit> = _appPaused.asSharedFlow()

    private var customTabLaunched = false

    fun handleAuthorizationCode(code: String) {
        _authorizationCode.tryEmit(code)
    }

    fun handleError(error: String) {
        _authorizationError.tryEmit(error)
    }

    fun notifyAppResumed() {
        _appResumed.tryEmit(Unit)
    }

    fun notifyAppPaused() {
        _appPaused.tryEmit(Unit)
    }

    fun setCustomTabLaunched() {
        customTabLaunched = true
    }

    fun resetCustomTabLaunched() {
        customTabLaunched = false
    }

    fun isCustomTabLaunched(): Boolean {
        return customTabLaunched
    }
}