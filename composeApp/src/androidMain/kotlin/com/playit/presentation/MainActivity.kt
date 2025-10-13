package com.playit.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.playit.constants.SpotifyConfig
import com.playit.utils.ChromeCustomTabsManager
import com.playit.utils.OAuthCallbackManager

class MainActivity : ComponentActivity() {

    private lateinit var chromeCustomTabsManager: ChromeCustomTabsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.WHITE,
                darkScrim = android.graphics.Color.BLACK,
            ),
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.WHITE,
                darkScrim = android.graphics.Color.BLACK,
            )
        )
        super.onCreate(savedInstanceState)

        chromeCustomTabsManager = ChromeCustomTabsManager(this, this)
        chromeCustomTabsManager.bindCustomTabsService()

        handleOAuthRedirect(intent)

        setContent {
            App(
                onLaunchOAuth = { url ->
                    chromeCustomTabsManager.openCustomTab(url)
                },
                onSetTabCloseListener = { callback ->
                    chromeCustomTabsManager.setOnTabClosedListener(callback)
                }
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        setIntent(intent) // Important: update the activity's intent
        handleOAuthRedirect(intent)
    }

    override fun onResume() {
        super.onResume()
        OAuthCallbackManager.notifyAppResumed()
    }

    override fun onPause() {
        super.onPause()
        OAuthCallbackManager.notifyAppPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        chromeCustomTabsManager.unbindCustomTabsService()
    }

    private fun handleOAuthRedirect(intent: Intent?) {

        intent?.data?.let { uri ->

            if (uri.toString().startsWith(SpotifyConfig.REDIRECT_URI)) {
                val code = uri.getQueryParameter("code")
                val error = uri.getQueryParameter("error")

                when {
                    code != null -> {
                        OAuthCallbackManager.handleAuthorizationCode(code)
                    }

                    error != null -> {
                        val errorDescription =
                            uri.getQueryParameter("error_description") ?: "Authorization failed"
                        OAuthCallbackManager.handleError(errorDescription)
                    }

                    else -> {
                        OAuthCallbackManager.handleError("Invalid authorization response")
                    }
                }
            }
        }
    }
}