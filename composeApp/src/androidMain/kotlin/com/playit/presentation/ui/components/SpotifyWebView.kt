package com.playit.presentation.ui.components

import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.playit.constants.SpotifyConfig
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpotifyWebView(
    onCodeReceived: (String) -> Unit,
    onError: (String) -> Unit,
    onCancelled: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Spotify Login") },
            navigationIcon = {
                IconButton(onClick = onCancelled) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = onCancelled) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        )

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.setSupportZoom(true)
                    settings.builtInZoomControls = true
                    settings.displayZoomControls = false

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            url?.let {
                                if (it.startsWith(SpotifyConfig.REDIRECT_URI)) {
                                    val uri = it.toUri()
                                    val code = uri.getQueryParameter("code")
                                    val error = uri.getQueryParameter("error")

                                    when {
                                        code != null -> {
                                            onCodeReceived(code)
                                            return true
                                        }
                                        error != null -> {
                                            val errorDescription = uri.getQueryParameter("error_description") ?: "Authorization failed"
                                            onError(errorDescription)
                                            return true
                                        }
                                        else -> {
                                            onError("Invalid authorization response")
                                            return true
                                        }
                                    }
                                }
                            }
                            return false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            errorCode: Int,
                            description: String?,
                            failingUrl: String?
                        ) {
                            super.onReceivedError(view, errorCode, description, failingUrl)
                            onError(description ?: "An error occurred")
                        }
                    }

                    loadUrl(SpotifyConfig.AUTH_URL + SpotifyConfig.URL_PARAMS)
                }
            }
        )
    }
}