package com.kolu.mediconnect.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kolu.mediconnect.data.repository.FirebaseAuthRepository
import com.kolu.mediconnect.presentation.screens.auth.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    viewModelOf(::AuthViewModel)

    singleOf(::FirebaseAuthRepository)

    single{FirebaseAuth.getInstance()}

    single { FirebaseFirestore.getInstance() }
}