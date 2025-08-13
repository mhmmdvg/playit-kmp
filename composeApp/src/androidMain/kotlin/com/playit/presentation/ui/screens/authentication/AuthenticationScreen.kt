package com.playit.presentation.ui.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.playit.presentation.ui.components.SpotifyWebView
import com.playit.presentation.ui.screens.authentication.components.AuthenticationContent
import com.playit.presentation.viewmodel.AuthenticationViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel


@Composable
@Preview
fun AuthenticationScreen(
    onSetTabCloseListener: (() -> Unit) -> Unit,
    onLaunchOAuth: (String) -> Unit,
    viewModel: AuthenticationViewModel = koinViewModel()
) {
    val authUiState by viewModel.authUiState.collectAsState()

//    LaunchedEffect(authUiState.isAuthenticated) {
//        if (authUiState.isAuthenticated) {
//            onAuthenticationSuccess()
//        }
//    }

    authUiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            viewModel.clearErrorMessage()
        }
    }

    AuthenticationContent(
        isLoading = authUiState.isLoading,
        errorMessage = authUiState.errorMessage,
        onSignIn = {
            viewModel.signIn(onLaunchOAuth, onSetTabCloseListener)
        }
    )


}
