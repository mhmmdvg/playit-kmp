package com.playit

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlayitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@PlayitApp)
            modules()
        }
    }
}