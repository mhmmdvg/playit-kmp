package com.playit.presentation.ui.screens.authentication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.playit.presentation.ui.components.SpotifyWebView
import com.playit.presentation.ui.screens.authentication.components.AuthenticationContent
import com.playit.presentation.viewmodel.AuthenticaitonViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.androidx.compose.koinViewModel


@Composable
@Preview
fun AuthenticationScreen(
    onAuthenticationSuccess: () -> Unit,
    viewModel: AuthenticaitonViewModel = koinViewModel()
) {
    val authUiState by viewModel.authUiState.collectAsState()
    val showWebView by viewModel.showWebView.collectAsState()

    LaunchedEffect(authUiState.isAuthenticated) {
        if (authUiState.isAuthenticated) {
            onAuthenticationSuccess()
        }
    }

    authUiState.errorMessage?.let { errorMessage ->
        LaunchedEffect(errorMessage) {
            viewModel.clearErrorMessage()
        }
    }

    if (showWebView) {
        SpotifyWebView(
            onCodeReceived = { code ->
                viewModel.onAuthorizationCodeReceived(code)
            },
            onError = { errorMessage ->
                viewModel.onWebViewError(errorMessage)
            },
            onCancelled = {
                viewModel.onWebViewCancelled()
            }
        )
    } else {
        AuthenticationContent(
            isLoading = authUiState.isLoading,
            errorMessage = authUiState.errorMessage,
        ) {
            viewModel.signIn()
        }
    }


}
