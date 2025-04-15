package com.kolu.mediconnect.presentation.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kolu.mediconnect.data.repository.UserRepo
import com.kolu.mediconnect.domain.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepo,

    ) : ViewModel() {
    private val _user = MutableStateFlow(
        UserData(
            userId = "",
            name = "",
            email = "",
            phoneNumber = "",
            age = 0,
            emergencyContact = ""
        )
    )
    val user = _user

    fun loadUserData(onFailure: (String) -> Unit = {}): UserData {
        try {
            viewModelScope.launch {
                val userData = userRepository.getUserData()
                _user.value = userData
            }
        } catch (e: Exception) {
            onFailure(e.message ?: "Unknown error")
        }
        return user.value
    }
}