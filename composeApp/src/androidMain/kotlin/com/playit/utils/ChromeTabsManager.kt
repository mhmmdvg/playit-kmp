package com.playit.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.net.toUri
import com.playit.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class ChromeCustomTabsManager(
    private val context: Context,
    private val activity: Activity
) {

    private var customTabsIntent: CustomTabsIntent? = null
    private var customTabsServiceConnection: CustomTabsServiceConnection? = null
    private var customTabsClient: androidx.browser.customtabs.CustomTabsClient? = null
    private var customTabsSession: CustomTabsSession? = null
    private val isTabOpen = AtomicBoolean(false)

    private var onTabClosed: (() -> Unit)? = null
    private var tabCloseDetectionJob: Job? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val customTabsCallback = object : CustomTabsCallback() {
        override fun onNavigationEvent(navigationEvent: Int, extras: Bundle?) {
            super.onNavigationEvent(navigationEvent, extras)

            when (navigationEvent) {
                NAVIGATION_FINISHED -> {
                    startTabCloseDetection()
                }
                TAB_HIDDEN -> {
                    handleTabClose()
                }
            }
        }
    }

    fun setOnTabClosedListener(listener: () -> Unit) {
        onTabClosed = listener
    }

    fun openCustomTab(url: String) {
        // Prevent multiple tabs from opening
        if (!isTabOpen.compareAndSet(false, true)) {
            return
        }

        try {

            customTabsSession = customTabsClient?.newSession(customTabsCallback)

            // Try multiple approaches for better compatibility
            when {
                tryCustomTabsWithSession(url) -> {
                    startTabCloseDetection()
                    return
                }
                tryCustomTabsWithActivity(url) -> {
                    startTabCloseDetection()
                    return
                }
                tryCustomTabsBasic(url) -> {
                    startTabCloseDetection()
                    return
                }
                else -> openInBrowser(url)
            }
        } catch (e: Exception) {
            isTabOpen.set(false)
            openInBrowser(url)
        }
    }

    fun closeCustomTab() {
        val intent = Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        activity.startActivity(intent)
        resetTabState()
    }

    private fun startTabCloseDetection() {
        tabCloseDetectionJob?.cancel()

        tabCloseDetectionJob = coroutineScope.launch {
            var wasAppInForeground = true

            while (isTabOpen.get()) {
                delay(500)

                val isAppInForeground = isAppInForeground()

                if (!wasAppInForeground && isAppInForeground) {
                    delay(1000)
                    if (isTabOpen.get()) {
                        handleTabClose()
                        break
                    }
                }

                wasAppInForeground = isAppInForeground
            }
        }
    }

    private fun isAppInForeground(): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses

        return runningAppProcesses?.any { processInfo ->
            processInfo.processName == context.packageName &&
                    processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        } ?: false
    }

    private fun handleTabClose() {
        if (isTabOpen.compareAndSet(true, false)) {
            tabCloseDetectionJob?.cancel()
            onTabClosed?.invoke()
        }
    }

    private fun tryCustomTabsWithSession(url: String): Boolean {
        return try {
            if (customTabsSession != null && context is Activity) {
                val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
                    .setShowTitle(true)
                    .setStartAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .setExitAnimations(context, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .build()

                customTabsIntent.launchUrl(context, url.toUri())
                true
            } else {
                false
            }
        } catch (error: Exception) {
            false
        }
    }

    private fun tryCustomTabsWithActivity(url: String): Boolean {
        return try {
            if (context is Activity) {
                val customTabsIntent = CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build()

                // Launch directly from Activity context
                customTabsIntent.launchUrl(context, url.toUri())
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
                customTabsIntent.launchUrl(context, url.toUri())
            } else {
                val intent = customTabsIntent.intent
                intent.data = url.toUri()
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
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resetTabState()
        }
    }

    fun resetTabState() {
        isTabOpen.set(false)
    }

    fun onAuthenticationComplete() {
        resetTabState()
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
                CustomTabsClient.bindCustomTabsService(
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
        val activityIntent = Intent(Intent.ACTION_VIEW, "https://www.example.com".toUri())
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