package com.playit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
//    onAuthenticationComplete: () -> Unit,
    onSetTabCloseListener: (() -> Unit) -> Unit
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
                    onSetTabCloseListener = onSetTabCloseListener,
                    onLaunchOAuth = onLaunchOAuth
                )
            }

            composable("home") {
                HomeScreen(
                    onSignOut = {
                        navController.navigate("authentication") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
//                    onAuthenticationComplete = onAuthenticationComplete
                )
            }
        }

    }
}