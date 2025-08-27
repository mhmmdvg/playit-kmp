package com.playit.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.playit.presentation.ui.components.AppBar
import com.playit.presentation.ui.components.NavigationTitle
import com.playit.presentation.ui.screens.authentication.AuthenticationScreen
import com.playit.presentation.ui.screens.home.HomeScreen
import com.playit.remote.repository.AuthenticationRepositoryImpl
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    onLaunchOAuth: (String) -> Unit,
    onSetTabCloseListener: (() -> Unit) -> Unit
) {

    val navController = rememberNavController()
    val authenticationRepositoryImpl: AuthenticationRepositoryImpl = koinInject()
    var scrollOffset by remember { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isAuthenticated by authenticationRepositoryImpl.authStateFlow.collectAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    val maxOffset = 200
    val collapseProgress = min(1f, max(0f, scrollOffset.toFloat() / maxOffset))
    val topPadding = (16 - (6 * collapseProgress)).dp


    LaunchedEffect(isAuthenticated) {
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
        if (authenticationRepositoryImpl.isUserLoggedIn()) Screen.HomeScreen.route else Screen.AuthenticationScreen.route

    MaterialTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .then(if (currentRoute == Screen.HomeScreen.route) Modifier.padding(top = topPadding) else Modifier),
            topBar = {
                when (currentRoute) {
                    Screen.HomeScreen.route -> {
                        AppBar(
                            title = "Home",
                            scrollOffset = scrollOffset,
                            maxOffset = maxOffset,
                        )
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        bottom = innerPadding.calculateBottomPadding()
                    ),
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
                    HomeScreen(
                        onScrollOffsetChanged = { offset ->
                            scrollOffset = offset
                        },
                        navigationTitle = {
                            NavigationTitle(
                                title = "Home",
                                scrollOffset = scrollOffset,
                                maxOffset = maxOffset,
                            )
                        }
                    )
                }
            }
        }
    }
}