package com.playit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.playit.presentation.ui.screens.authentication.AuthenticationScreen
import com.playit.presentation.ui.screens.home.HomeScreen
import com.playit.remote.repository.AuthenticationRepository
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

import playit.composeapp.generated.resources.Res
import playit.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(
    onLaunchOAuth: (String) -> Unit
) {

    val navController = rememberNavController()
    val authenticationRepository: AuthenticationRepository = koinInject()

    val startDestination = if (authenticationRepository.isUserLoggedIn()) {
        "home"
    } else {
        "authentication"
    }

    MaterialTheme {

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.fillMaxSize()
        ) {
            composable("authentication") {
                AuthenticationScreen(
                    onAuthenticationSuccess = {
                        navController.navigate("home") {
                            popUpTo("authentication") { inclusive = true }
                        }
                    },
                    onLaunchOAuth = onLaunchOAuth
                )
            }

            composable("home") {
                HomeScreen(
                    onSignOut = {
                        navController.navigate("authentication") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }

    }
}