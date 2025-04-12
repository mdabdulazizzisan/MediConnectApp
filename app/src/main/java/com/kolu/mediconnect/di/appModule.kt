package com.kolu.mediconnect.di

import com.kolu.mediconnect.navigation.StartDestinationViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::StartDestinationViewModel)
}