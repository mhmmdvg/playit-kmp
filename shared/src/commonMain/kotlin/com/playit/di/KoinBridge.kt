package com.playit.di

import org.koin.core.context.startKoin

class KoinBridge {
    fun initKoin() {
        startKoin {
            modules(sharedModules())
        }
    }
}