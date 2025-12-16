package com.playit.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.navigation.compose.*
import com.playit.data.remote.repository.AuthenticationRepositoryImpl
import com.playit.presentation.ui.components.AppBar
import com.playit.presentation.ui.components.BottomNavigation
import com.playit.presentation.ui.components.NavigationTitle
import com.playit.presentation.ui.screens.authentication.AuthenticationScreen
import com.playit.presentation.ui.screens.home.HomeScreen
import com.playit.presentation.ui.screens.library.LibraryScreen
import com.playit.presentation.ui.screens.profile.ProfileScreen
import com.playit.presentation.ui.screens.search.SearchScreen
import com.playit.presentation.ui.theme.DarkColorScheme
import com.playit.presentation.ui.theme.LightColorScheme
import com.playit.viewmodels.CurrentPlaylistsViewModel
import com.playit.viewmodels.NewReleasesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App(
    darkTheme: Boolean = isSystemInDarkTheme(),
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


    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated && currentRoute == Screen.AuthenticationScreen.route) {
            navController.navigate(Screen.BottomNav.route) {
                popUpTo(Screen.AuthenticationScreen.route) { inclusive = true }
            }
        } else if (!isAuthenticated && currentRoute == Screen.BottomNav.route) {
            navController.navigate(Screen.AuthenticationScreen.route) {
                popUpTo(0)
            }
        }
    }

    val startDestination =
        if (authenticationRepositoryImpl.isUserLoggedIn()) Screen.BottomNav.route else Screen.AuthenticationScreen.route
    val shouldBottomNav = when (currentRoute) {
        Screen.AuthenticationScreen.route -> false
        null -> false
        else -> true
    }

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                when (currentRoute) {
                    Screen.HomeScreen.route -> {
                        AppBar(
                            title = "Home",
                            scrollOffset = scrollOffset,
                            maxOffset = maxOffset,
                        )
                    }

                    Screen.LibraryScreen.route -> {
                        AppBar(
                            title = "Library",
                            scrollOffset = scrollOffset,
                            maxOffset = maxOffset,
                        )
                    }

                    Screen.ProfileScreen.route -> {
                        AppBar(
                            title = "",
                            scrollOffset = 200,
                            maxOffset = maxOffset,
                            onBackPressed = { navController.popBackStack() }
                        )
                    }
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = shouldBottomNav,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    BottomNavigation(navController)
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

                        targetState.destination.route == Screen.HomeScreen.route || targetState.destination.route == Screen.SearchScreen.route || targetState.destination.route == Screen.LibraryScreen.route -> fadeIn(
                            animationSpec = tween(300)
                        )

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

                        targetState.destination.route == Screen.HomeScreen.route || targetState.destination.route == Screen.SearchScreen.route || targetState.destination.route == Screen.LibraryScreen.route -> fadeOut(
                            animationSpec = tween(300)
                        )

                        else -> slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(350)
                        )
                    }
                },
                popEnterTransition = {
                    // Reverse of exitTransition - slide in from left
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                },
                popExitTransition = {
                    // Reverse of enterTransition - slide out to right
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(350)
                    )
                }
            ) {
                composable("authentication") {
                    AuthenticationScreen(
                        onSetTabCloseListener = onSetTabCloseListener,
                        onLaunchOAuth = onLaunchOAuth
                    )
                }

                navigation(
                    startDestination = Screen.HomeScreen.route,
                    route = Screen.BottomNav.route
                ) {
                    composable("home") {
                        val parentEntry = remember(it) {
                            navController.getBackStackEntry(Screen.BottomNav.route)
                        }

                        val currentPlaylistsViewModel: CurrentPlaylistsViewModel =
                            koinViewModel<CurrentPlaylistsViewModel>(viewModelStoreOwner = parentEntry)
                        val newReleaseViewModel: NewReleasesViewModel =
                            koinViewModel<NewReleasesViewModel>(viewModelStoreOwner = parentEntry)

                        HomeScreen(
                            onScrollOffsetChanged = { offset ->
                                scrollOffset = offset
                            },
                            navigationTitle = {
                                NavigationTitle(
                                    title = "Home",
                                    navController = navController,
                                    scrollOffset = scrollOffset,
                                    maxOffset = maxOffset,
                                )
                            },
                            newReleaseVm = newReleaseViewModel,
                            currentPlaylistVm = currentPlaylistsViewModel,
                        )
                    }

                    composable("search") {
                        SearchScreen()
                    }

                    composable("library") {
                        val parentEntry = remember(it) {
                            navController.getBackStackEntry(Screen.BottomNav.route)
                        }

                        val currentPlaylistsViewModel: CurrentPlaylistsViewModel =
                            koinViewModel<CurrentPlaylistsViewModel>(viewModelStoreOwner = parentEntry)

                        LibraryScreen(
                            onScrollOffsetChanged = { offset ->
                                scrollOffset = offset
                            },
                            navigationTitle = {
                                NavigationTitle(
                                    title = "Library",
                                    navController = navController,
                                    scrollOffset = scrollOffset,
                                    maxOffset = maxOffset,
                                )
                            },
                            currentPaylistVm = currentPlaylistsViewModel,
                        )
                    }
                }

                composable("profile") {
                    ProfileScreen()
                }
            }
        }
    }
}