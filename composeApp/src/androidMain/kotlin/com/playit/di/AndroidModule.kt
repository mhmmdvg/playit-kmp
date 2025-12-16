package com.playit.di

import com.playit.presentation.viewmodel.AuthenticationViewModel
import com.playit.viewmodels.NewReleasesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val androidModule = module {
    viewModelOf(::AuthenticationViewModel)
}