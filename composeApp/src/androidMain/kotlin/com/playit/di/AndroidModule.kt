package com.playit.di

import com.playit.presentation.viewmodel.AuthenticaitonViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {
    viewModel { AuthenticaitonViewModel(get()) }
}