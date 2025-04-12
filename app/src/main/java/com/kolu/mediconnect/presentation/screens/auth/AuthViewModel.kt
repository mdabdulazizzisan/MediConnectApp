package com.kolu.mediconnect.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolu.mediconnect.data.repository.FirebaseAuthRepository
import com.kolu.mediconnect.domain.model.UserData
import com.kolu.mediconnect.domain.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val _userData: MutableStateFlow<UserData> = MutableStateFlow(UserData("", "", "", "+88", 0, "+88"))
    val userData = _userData.asStateFlow()

    private val _password: MutableStateFlow<String> = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authUiState = _authUiState.asStateFlow()

    fun setUserData(userData: UserData) {
        _userData.value = userData
    }

    fun setName(name: String) {
        _userData.update { it.copy(name = name) }
    }

    fun setEmail(email: String) {
        _userData.update { it.copy(email = email) }
    }

    fun setPhoneNumber(phoneNumber: String) {
        _userData.update { it.copy(phoneNumber = phoneNumber) }
    }
    fun setAge(age: Int) {
        _userData.update { it.copy(age = age) }
    }
    fun setEmergencyContact(emergencyContact: String) {
        _userData.update { it.copy(emergencyContact = emergencyContact) }
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun login() {
        viewModelScope.launch {
            if (_userData.value.email.isEmpty() || _password.value.isEmpty()) {
                _authUiState.value = AuthUiState.Error("Please fill in all the fields")
                return@launch
            }
            _authUiState.value = AuthUiState.Loading
            when(val result = authRepository.login(_userData.value.email, _password.value)){
                is AuthResult.Success ->
                    _authUiState.value = AuthUiState.Success
                is AuthResult.Error ->
                    _authUiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun register(){
        viewModelScope.launch {
            if (_userData.value.run {
                    name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || age == 0 || emergencyContact.isEmpty()
                } || _password.value.isEmpty()) {
                _authUiState.value = AuthUiState.Error("Please fill in all the fields")
                return@launch
            }
            _authUiState.value = AuthUiState.Loading
            when(val result = authRepository.register(_userData.value, _password.value)){
                is AuthResult.Success ->
                    _authUiState.value = AuthUiState.Success
                is AuthResult.Error ->
                    _authUiState.value = AuthUiState.Error(result.message)
            }
        }
    }

    fun resetState() {
        _authUiState.value = AuthUiState.Idle
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}

sealed class AuthUiState {
    data object Idle : AuthUiState()
    data object Loading : AuthUiState()
    data class Error(val message: String) : AuthUiState()
    data object Success : AuthUiState()
}