package com.playit.presentation

sealed class Screen(val route: String) {
    object AuthenticationScreen : Screen("authentication")
    object HomeScreen : Screen("home")
}