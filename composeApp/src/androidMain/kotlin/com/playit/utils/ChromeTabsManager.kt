package com.playit.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.core.net.toUri
import com.playit.constants.SpotifyConfig
import java.util.concurrent.atomic.AtomicBoolean

class ChromeCustomTabsManager(private val context: Context) {

    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var customTabsClient: androidx.browser.customtabs.CustomTabsClient? = null
    private val isTabOpen = AtomicBoolean(false)

    fun openCustomTab(url: String) {
        // Prevent multiple tabs from opening
        if (!isTabOpen.compareAndSet(false, true)) {
            return
        }

        try {
            // Try multiple approaches for better compatibility
            when {
                tryCustomTabsWithActivity(url) -> return
                tryCustomTabsBasic(url) -> return
                else -> openInBrowser(url)
            }
        } catch (e: Exception) {
            isTabOpen.set(false)
            openInBrowser(url)
        }
    }

    private fun tryCustomTabsWithActivity(url: String): Boolean {
        return try {
            if (context is Activity) {
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()

                // Launch directly from Activity context
                customTabsIntent.launchUrl(context, Uri.parse(url))
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun tryCustomTabsBasic(url: String): Boolean {
        return try {
            val builder = CustomTabsIntent.Builder()

            // Minimal configuration to avoid compatibility issues
            val customTabsIntent = builder.build()

            // Ensure we're using the right context
            if (context is Activity) {
                customTabsIntent.launchUrl(context, Uri.parse(url))
            } else {
                val intent = customTabsIntent.intent
                intent.data = Uri.parse(url)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun openInBrowser(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun resetTabState() {
        isTabOpen.set(false)
    }

    // Simplified service binding
    fun bindCustomTabsService() {
        val packageName = getCustomTabsPackages().firstOrNull()
        if (packageName != null) {
            customTabsServiceConnection = object : CustomTabsServiceConnection() {
                override fun onCustomTabsServiceConnected(
                    name: android.content.ComponentName,
                    client: androidx.browser.customtabs.CustomTabsClient
                ) {
                    customTabsClient = client
                    client.warmup(0)
                }

                override fun onServiceDisconnected(name: android.content.ComponentName) {
                    customTabsClient = null
                }
            }

            try {
                androidx.browser.customtabs.CustomTabsClient.bindCustomTabsService(
                    context,
                    packageName,
                    customTabsServiceConnection!!
                )
            } catch (e: Exception) {
                customTabsServiceConnection = null
            }
        }
    }

    fun unbindCustomTabsService() {
        customTabsServiceConnection?.let { connection ->
            try {
                context.unbindService(connection)
            } catch (e: IllegalArgumentException) {
                // Service was not bound, ignore
            }
            customTabsServiceConnection = null
            customTabsClient = null
        }
        resetTabState()
    }

    private fun getCustomTabsPackages(): List<String> {
        val pm = context.packageManager
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
        val resolveInfos = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = mutableListOf<String>()

        for (info in resolveInfos) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)

            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        return packagesSupportingCustomTabs
    }
}