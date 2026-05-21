package com.finvoraai.personalfinancemanager.finvora.feature.auth

import kotlinx.coroutines.flow.Flow

data class AuthUser(val id: String, val email: String)

expect class AuthManager() {
    suspend fun signUp(email: String, password: String)
    suspend fun verifyEmail(code: String)
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    fun observeUser(): Flow<AuthUser?>
    suspend fun signInWithGoogle()
    suspend fun forgotPassword(email: String)
    suspend fun resetPassword(code: String, newPassword: String)
}
