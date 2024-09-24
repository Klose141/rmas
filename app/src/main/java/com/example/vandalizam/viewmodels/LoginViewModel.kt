package com.example.vandalizam.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vandalizam.models.User
import com.example.vandalizam.services.AuthService
import kotlinx.coroutines.launch

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class LoginUIEvent {
    data class EmailChanged(val email: String) : LoginUIEvent()
    data class PasswordChanged(val password: String) : LoginUIEvent()
    object Login : LoginUIEvent()
}

class LoginViewModel : ViewModel() {
    var state by mutableStateOf(LoginUIState())
        private set

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is LoginUIEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is LoginUIEvent.Login -> {
                login(state.email, state.password)
            }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            try {
                AuthService.login(email, password)
                state = state.copy(isLoading = false)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, error = e.message)
            }
        }
    }
}
