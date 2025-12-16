package com.playit.presentation

sealed class Screen(val route: String) {
    object AuthenticationScreen : Screen("authentication")
    object BottomNav : Screen("bottom_nav")
    object HomeScreen : Screen("home")
    object SearchScreen : Screen("search")
    object ProfileScreen : Screen("profile")
    object LibraryScreen : Screen("library")
}