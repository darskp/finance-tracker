package com.finvoraai.personalfinancemanager.finvora.feature.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class AuthManager actual constructor() {

    actual suspend fun signUp(email: String, password: String) {
        // Placeholder for iOS Clerk SDK integration
    }

    actual suspend fun verifyEmail(code: String) {
        // Placeholder for iOS
    }

    actual suspend fun signIn(email: String, password: String) {
        // Placeholder for iOS
    }

    actual suspend fun signOut() {
        // Placeholder for iOS
    }

    actual fun observeUser(): Flow<AuthUser?> {
        // Placeholder
        return flowOf(null)
    }

    actual suspend fun signInWithGoogle() {
        // Placeholder for iOS
    }

    actual suspend fun forgotPassword(email: String) {
        // Placeholder for iOS
    }

    actual suspend fun resetPassword(code: String, newPassword: String) {
        // Placeholder for iOS
    }
}
