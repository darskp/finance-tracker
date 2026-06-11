package com.finvoraai.personalfinancemanager.finvora.feature.auth

import kotlinx.coroutines.flow.Flow

data class AuthUser(val id: String, val email: String)

sealed interface SignInResult {
    data object Complete : SignInResult
    data object ClientTrustCodeSent : SignInResult
}

expect class AuthManager() {
    suspend fun signUp(email: String, password: String)
    suspend fun verifyEmail(code: String)
    suspend fun signIn(email: String, password: String): SignInResult
    suspend fun verifyClientTrustCode(code: String)
    suspend fun signOut()
    suspend fun getToken(): String?
    fun observeUser(): Flow<AuthUser?>
    fun observeIsInitialized(): Flow<Boolean>
    suspend fun signInWithGoogle()
    suspend fun signInWithApple()
    suspend fun forgotPassword(email: String)
    suspend fun resetPassword(code: String, newPassword: String)
}
