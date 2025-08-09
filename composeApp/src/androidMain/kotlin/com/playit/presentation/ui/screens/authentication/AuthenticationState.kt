package com.playit.presentation.ui.screens.authentication

data class AuthenticationState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false,
)
