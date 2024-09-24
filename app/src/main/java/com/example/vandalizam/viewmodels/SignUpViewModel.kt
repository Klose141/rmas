package com.example.vandalizam.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandalizam.services.AuthService
import kotlinx.coroutines.launch

data class SignUpUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class SingUpUIEvent {
    data class EmailChanged(val email: String) : SingUpUIEvent()
    data class PasswordChanged(val password: String) : SingUpUIEvent()
    object SignUp : SingUpUIEvent()
}

class SignUpViewModel : ViewModel() {
    var state by mutableStateOf(SignUpUIState())
        private set

    fun onEvent(event: SingUpUIEvent) {
        when (event) {
            is SingUpUIEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is SingUpUIEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is SingUpUIEvent.SignUp -> {
                signUp(state.email, state.password)
            }
        }
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                AuthService.signUp(email, password)
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}
