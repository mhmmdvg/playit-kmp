package com.playit.di

import com.playit.presentation.viewmodel.AuthenticationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val androidModule = module {
    viewModelOf(::AuthenticationViewModel)
}