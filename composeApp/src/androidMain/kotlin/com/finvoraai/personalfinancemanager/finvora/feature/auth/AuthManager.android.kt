package com.finvoraai.personalfinancemanager.finvora.feature.auth

import com.clerk.api.Clerk
import com.clerk.api.network.serialization.errorMessage
import com.clerk.api.network.serialization.onFailure
import com.clerk.api.network.serialization.onSuccess
import com.clerk.api.signin.SignIn
import com.clerk.api.signin.attemptFirstFactor
import com.clerk.api.signin.resetPassword
import com.clerk.api.signup.SignUp
import com.clerk.api.signup.attemptVerification
import com.clerk.api.signup.prepareVerification
import com.clerk.api.sso.OAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

actual class AuthManager actual constructor() {

    actual suspend fun signUp(email: String, password: String) {
        println("CLERK: >>> signUp(email=$email)")
        Clerk.auth.signUp {
            this.email = email
            this.password = password
        }
            .onSuccess { signUp ->
                println(
                    "CLERK: <<< signUp SUCCESS status=${signUp.status}" +
                        " id=${signUp.id} missingFields=${signUp.missingFields}" +
                        " unverifiedFields=${signUp.unverifiedFields}"
                )
                if (signUp.status != SignUp.Status.COMPLETE) {
                    println("CLERK: >>> prepareVerification(EmailCode)")
                    signUp.prepareVerification(SignUp.PrepareVerificationParams.Strategy.EmailCode())
                        .onSuccess {
                            println("CLERK: <<< prepareVerification SUCCESS status=${it.status}")
                        }
                        .onFailure { err ->
                            println("CLERK: <<< prepareVerification FAILED: ${err.errorMessage}")
                            throw Exception(err.errorMessage)
                        }
                }
            }
            .onFailure {
                println("CLERK: <<< signUp FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    actual suspend fun verifyEmail(code: String) {
        println("CLERK: >>> verifyEmail(code=$code)")
        val signUp = Clerk.auth.currentSignUp ?: run {
            println("CLERK: <<< verifyEmail FAILED - no sign up in progress")
            throw Exception("No sign up in progress")
        }
        println("CLERK: currentSignUp id=${signUp.id} status=${signUp.status}")
        signUp.attemptVerification(SignUp.AttemptVerificationParams.EmailCode(code))
            .onSuccess { result ->
                println(
                    "CLERK: <<< verifyEmail SUCCESS" +
                        " status=${result.status}" +
                        " createdSessionId=${result.createdSessionId}" +
                        " createdUserId=${result.createdUserId}"
                )
            }
            .onFailure {
                println("CLERK: <<< verifyEmail FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    actual suspend fun signIn(email: String, password: String) {
        println("CLERK: >>> signIn(email=$email)")
        Clerk.auth.signInWithPassword {
            identifier = email
            this.password = password
        }
            .onSuccess { signIn ->
                println(
                    "CLERK: <<< signIn SUCCESS id=${signIn.id}" +
                        " status=${signIn.status} createdSessionId=${signIn.createdSessionId}"
                )
            }
            .onFailure {
                println("CLERK: <<< signIn FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    private var currentResetSignIn: SignIn? = null

    actual suspend fun forgotPassword(email: String) {
        println("CLERK: >>> forgotPassword(email=$email)")
        SignIn.create(SignIn.CreateParams.Strategy.ResetPasswordEmailCode(identifier = email))
            .onSuccess { signIn ->
                println(
                    "CLERK: <<< forgotPassword SUCCESS id=${signIn.id}" +
                        " status=${signIn.status}"
                )
                currentResetSignIn = signIn
            }
            .onFailure {
                println("CLERK: <<< forgotPassword FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    @Suppress("ThrowsCount")
    actual suspend fun resetPassword(code: String, newPassword: String) {
        val signIn = currentResetSignIn
            ?: throw Exception("No password reset in progress")
        println("CLERK: >>> attemptFirstFactor(code=$code)")
        signIn.attemptFirstFactor(
            SignIn.AttemptFirstFactorParams.ResetPasswordEmailCode(code = code)
        )
            .onSuccess {
                println("CLERK: <<< attemptFirstFactor SUCCESS status=${it.status}")
            }
            .onFailure {
                println("CLERK: <<< attemptFirstFactor FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
        println("CLERK: >>> resetPassword(newPassword)")
        signIn.resetPassword(newPassword = newPassword, signOutOfOtherSessions = true)
            .onSuccess {
                println("CLERK: <<< resetPassword SUCCESS")
                currentResetSignIn = null
            }
            .onFailure {
                println("CLERK: <<< resetPassword FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    actual suspend fun signOut() {
        println("CLERK: >>> signOut()")
        Clerk.auth.signOut()
            .onSuccess { println("CLERK: <<< signOut SUCCESS") }
            .onFailure {
                println("CLERK: <<< signOut FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }

    actual fun observeUser(): Flow<AuthUser?> {
        println("CLERK: observeUser() called, current user=${Clerk.user?.id}")
        return Clerk.userFlow.map { user ->
            val email = user?.primaryEmailAddress?.emailAddress ?: ""
            val authUser = user?.let { AuthUser(id = it.id, email = email) }
            println("CLERK: userFlow emitted user=${authUser?.id} email=${authUser?.email}")
            authUser
        }
    }

    actual suspend fun signInWithGoogle() {
        println("CLERK: >>> signInWithGoogle()")
        Clerk.auth.signInWithOAuth(OAuthProvider.GOOGLE)
            .onSuccess { println("CLERK: <<< signInWithGoogle SUCCESS") }
            .onFailure {
                println("CLERK: <<< signInWithGoogle FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }
}
