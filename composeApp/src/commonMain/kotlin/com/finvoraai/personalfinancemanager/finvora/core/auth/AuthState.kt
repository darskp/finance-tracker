package com.finvoraai.personalfinancemanager.finvora.core.auth

sealed interface AuthState {
    data object Loading : AuthState
    data object LoggedOut : AuthState
    data class LoggedIn(val userId: String, val email: String) : AuthState
}

data class UserSession(
    val isLoggedIn: Boolean,
    val userId: String?,
    val email: String?
)
