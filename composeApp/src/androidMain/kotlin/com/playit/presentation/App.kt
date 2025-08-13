package com.playit.presentation

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playit.presentation.ui.screens.authentication.AuthenticationScreen
import com.playit.presentation.ui.screens.home.HomeScreen
import com.playit.remote.repository.AuthenticationRepository
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
@Preview
fun App(
    onLaunchOAuth: (String) -> Unit,
    onSetTabCloseListener: (() -> Unit) -> Unit
) {

    val navController = rememberNavController()
    val authenticationRepository: AuthenticationRepository = koinInject()

    val isAuthenticated by authenticationRepository.authStateFlow.collectAsState()

    LaunchedEffect(isAuthenticated) {
        val currentRoute = navController.currentDestination?.route

        if (isAuthenticated && currentRoute == Screen.AuthenticationScreen.route) {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.AuthenticationScreen.route) { inclusive = true }
            }
        } else if (!isAuthenticated && currentRoute == Screen.HomeScreen.route) {
            navController.navigate(Screen.AuthenticationScreen.route) {
                popUpTo(0)
            }
        }
    }

    val startDestination =
        if (authenticationRepository.isUserLoggedIn()) Screen.HomeScreen.route else Screen.AuthenticationScreen.route

    MaterialTheme {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                when {
                    (initialState.destination.route == Screen.AuthenticationScreen.route &&
                            targetState.destination.route == Screen.HomeScreen.route) -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(durationMillis = 400)
                        ) + fadeIn(animationSpec = tween(400))
                    }

                    (initialState.destination.route == Screen.HomeScreen.route &&
                            targetState.destination.route == Screen.AuthenticationScreen.route) -> {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(durationMillis = 400)
                        ) + fadeIn(animationSpec = tween(400))
                    }

                    else -> slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350)
                    )
                }
            },
            exitTransition = {
                when {
                    (initialState.destination.route == Screen.AuthenticationScreen.route &&
                            targetState.destination.route == Screen.HomeScreen.route) -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Up,
                            animationSpec = tween(durationMillis = 400)
                        ) + fadeOut(animationSpec = tween(400))
                    }

                    (initialState.destination.route == Screen.HomeScreen.route &&
                            targetState.destination.route == Screen.AuthenticationScreen.route) -> {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Down,
                            animationSpec = tween(durationMillis = 400)
                        ) + fadeOut(animationSpec = tween(400))
                    }

                    else -> slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(350)
                    )
                }
            }
        ) {
            composable("authentication") {
                AuthenticationScreen(
                    onSetTabCloseListener = onSetTabCloseListener,
                    onLaunchOAuth = onLaunchOAuth
                )
            }

            composable("home") {
                HomeScreen()
            }
        }

    }
}