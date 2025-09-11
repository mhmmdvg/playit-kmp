package com.playit

import android.app.Application
import com.playit.data.cache.DataStoreFactory
import com.playit.di.androidModule
import com.playit.di.initKoin
import com.playit.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PlayitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        DataStoreFactory.initialize(this)

        initKoin(
            platformModules = androidModule
        ) {
            androidContext(this@PlayitApp)
        }
    }
}