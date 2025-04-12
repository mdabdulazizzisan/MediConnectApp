package com.kolu.mediconnect.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolu.mediconnect.data.repository.FirebaseAuthRepository
import com.kolu.mediconnect.domain.repository.AuthResult
import com.kolu.mediconnect.presentation.screens.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StartDestinationViewModel(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {
    private val _viewState =
        MutableStateFlow<StartDestinationViewState>(StartDestinationViewState.Loading)
    val viewState: StateFlow<StartDestinationViewState> = _viewState.asStateFlow()

    private val hasLoggedIn = MutableStateFlow<Boolean?>(null)

    init {
        viewModelScope.launch {
            hasLoggedIn.value = authRepository.isUserLoggedIn()
            hasLoggedIn.map { isLoggedIn ->
                when (isLoggedIn) {
                    null -> StartDestinationViewState.Loading
                    true -> StartDestinationViewState.LoggedIN
                    false -> StartDestinationViewState.NotLoggedIn
                }
            }.collect { state ->
                _viewState.value = state
            }
        }
    }
    fun logout() {
        authRepository.logout()
        _viewState.value = StartDestinationViewState.NotLoggedIn
    }

    sealed class StartDestinationViewState {
        data object Loading : StartDestinationViewState()
        data object LoggedIN : StartDestinationViewState()
        data object NotLoggedIn : StartDestinationViewState()
    }
}