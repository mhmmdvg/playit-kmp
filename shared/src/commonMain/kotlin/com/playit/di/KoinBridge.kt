package com.playit.di

import org.koin.core.context.startKoin

class KoinBridge {
    fun doInitKoin() {
        startKoin {
            modules(sharedModules())
        }
    }
}