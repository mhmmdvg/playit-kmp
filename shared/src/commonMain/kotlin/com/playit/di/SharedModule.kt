package com.playit.di

import org.koin.core.module.Module

fun sharedModules(): List<Module> = listOf(
    networkModule,
    localModule(),
    appModule,
)