package com.finvoraai.personalfinancemanager.finvora.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface AuthEvent {
    data object NavigateToDashboard : AuthEvent
}

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
    data object PasswordResetCodeSent : AuthUiState
    data class VerificationRequired(val error: String? = null) : AuthUiState
}

@Suppress("TooGenericExceptionCaught")
class AuthViewModel(
    private val authManager: AuthManager = AuthManager()
) : ViewModel() {

    val user = authManager.observeUser().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        null
    )

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events: SharedFlow<AuthEvent> = _events.asSharedFlow()

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.signUp(email, password)
                _uiState.value = AuthUiState.VerificationRequired()
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun verifyEmail(code: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.verifyEmail(code)
                _uiState.value = AuthUiState.Idle
                _events.emit(AuthEvent.NavigateToDashboard)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.VerificationRequired(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.signIn(email, password)
                _uiState.value = AuthUiState.Idle
                _events.emit(AuthEvent.NavigateToDashboard)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            try {
                authManager.signInWithGoogle()
                _events.emit(AuthEvent.NavigateToDashboard)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: GOOGLE_ERROR)
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.forgotPassword(email)
                _uiState.value = AuthUiState.PasswordResetCodeSent
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.resetPassword(code, newPassword)
                _uiState.value = AuthUiState.Idle
                _events.emit(AuthEvent.NavigateToDashboard)
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                authManager.signOut()
                _uiState.value = AuthUiState.Idle
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun dismissVerificationError() {
        val current = _uiState.value
        if (current is AuthUiState.VerificationRequired) {
            _uiState.value = AuthUiState.VerificationRequired(null)
        }
    }

    fun dismissResetState() {
        val current = _uiState.value
        if (current is AuthUiState.PasswordResetCodeSent) {
            _uiState.value = AuthUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    companion object {
        private const val UNKNOWN_ERROR = "Unknown error"
        private const val GOOGLE_ERROR = "Google Sign In Failed"
    }
}
