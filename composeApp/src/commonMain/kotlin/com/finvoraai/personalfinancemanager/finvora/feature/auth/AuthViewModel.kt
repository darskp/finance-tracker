package com.finvoraai.personalfinancemanager.finvora.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.signUp(email, password)
                _uiState.value = AuthUiState.NeedsVerification
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
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            try {
                authManager.signIn(email, password)
                _uiState.value = AuthUiState.Success
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: UNKNOWN_ERROR)
            }
        }
    }

    fun googleSignIn() {
        viewModelScope.launch {
            try {
                authManager.signInWithGoogle()
            } catch (e: Exception) {
                _uiState.value = AuthUiState.Error(e.message ?: GOOGLE_ERROR)
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

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }

    companion object {
        private const val UNKNOWN_ERROR = "Unknown error"
        private const val GOOGLE_ERROR = "Google Sign In Failed"
    }
}

sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Error(val message: String) : AuthUiState
    data object NeedsVerification : AuthUiState
    data object Success : AuthUiState
}
