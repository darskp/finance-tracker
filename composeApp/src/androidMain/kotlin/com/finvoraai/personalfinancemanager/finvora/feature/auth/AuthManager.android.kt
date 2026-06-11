package com.finvoraai.personalfinancemanager.finvora.feature.auth

import com.clerk.api.Clerk
import com.clerk.api.network.serialization.errorMessage
import com.clerk.api.network.serialization.onFailure
import com.clerk.api.network.serialization.onSuccess
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import kotlinx.coroutines.delay
import com.clerk.api.signin.SignIn
import com.clerk.api.signin.attemptFirstFactor
import com.clerk.api.signin.attemptSecondFactor
import com.clerk.api.signin.prepareSecondFactor
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

    private var pendingClientTrustSignIn: SignIn? = null

    @Suppress("ThrowsCount")
    actual suspend fun signIn(email: String, password: String): SignInResult {
        println("CLERK: >>> signIn(email=$email)")
        var signInResult: SignInResult? = null
        Clerk.auth.signInWithPassword {
            identifier = email
            this.password = password
        }
            .onSuccess { signIn ->
                println(
                    "CLERK: <<< signIn SUCCESS id=${signIn.id}" +
                        " status=${signIn.status} createdSessionId=${signIn.createdSessionId}"
                )
                signInResult = when (signIn.status) {
                    SignIn.Status.COMPLETE -> {
                        val sessionId = signIn.createdSessionId
                            ?: throw Exception("Sign-in complete but no session ID")
                        Clerk.auth.setActive(sessionId)
                            .onFailure { throw Exception(it.errorMessage) }
                        println("CLERK: session activated id=$sessionId")
                        SignInResult.Complete
                    }
                    SignIn.Status.NEEDS_CLIENT_TRUST -> {
                        val emailFactor = signIn.supportedSecondFactors?.firstOrNull {
                            it.strategy == "email_code"
                        } ?: throw Exception(
                            "Client trust required but email_code second factor unavailable"
                        )
                        pendingClientTrustSignIn = signIn
                        println(
                            "CLERK: preparing second factor strategy=email_code" +
                                " emailAddressId=${emailFactor.emailAddressId}"
                        )
                        signIn.prepareSecondFactor("email_code")
                            .onSuccess {
                                println("CLERK: second factor prepared, code sent to email")
                            }
                            .onFailure {
                                println("CLERK: prepareSecondFactor FAILED: ${it.errorMessage}")
                                throw Exception(it.errorMessage)
                            }
                        SignInResult.ClientTrustCodeSent
                    }
                    else -> throw Exception("Unexpected sign-in status: ${signIn.status}")
                }
            }
            .onFailure {
                println("CLERK: <<< signIn FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
        return signInResult!!
    }

    @Suppress("ThrowsCount")
    actual suspend fun verifyClientTrustCode(code: String) {
        val signIn = pendingClientTrustSignIn
            ?: throw Exception("No client trust sign-in in progress")
        println("CLERK: >>> verifyClientTrustCode(code=$code)")
        signIn.attemptSecondFactor(
            SignIn.AttemptSecondFactorParams.EmailCode(code = code)
        )
            .onSuccess { updatedSignIn ->
                println(
                    "CLERK: <<< attemptSecondFactor SUCCESS" +
                        " status=${updatedSignIn.status}" +
                        " createdSessionId=${updatedSignIn.createdSessionId}"
                )
                if (updatedSignIn.status != SignIn.Status.COMPLETE) {
                    throw Exception(
                        "Client trust verification failed: status=${updatedSignIn.status}"
                    )
                }
                val sessionId = updatedSignIn.createdSessionId
                    ?: throw Exception("Verification complete but no session ID")
                Clerk.auth.setActive(sessionId)
                    .onFailure { throw Exception(it.errorMessage) }
                println("CLERK: session activated id=$sessionId")
                pendingClientTrustSignIn = null
            }
            .onFailure {
                println("CLERK: <<< attemptSecondFactor FAILED: ${it.errorMessage}")
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

    actual suspend fun getToken(): String? {
        return try {
            // Retry up to 5 times with short delay to wait for Clerk to load the JWT
            var token: String? = null
            for (i in 1..5) {
                token = Clerk.session?.lastActiveToken?.jwt
                if (token != null) break
                delay(200)
            }
            println("CLERK: <<< getToken ${if (token != null) "SUCCESS" else "null after retries"}")
            DebugLogger.auth("getToken", if (token != null) "OK (${token.take(20)}...)" else "null after retries")
            token
        } catch (e: Exception) {
            println("CLERK: <<< getToken FAILED: ${e.message}")
            DebugLogger.auth("getToken", "ERROR: ${e.message}")
            null
        }
    }

    actual fun observeIsInitialized(): Flow<Boolean> = Clerk.isInitialized

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

    actual suspend fun signInWithApple() {
        println("CLERK: >>> signInWithApple()")
        Clerk.auth.signInWithOAuth(OAuthProvider.APPLE)
            .onSuccess { println("CLERK: <<< signInWithApple SUCCESS") }
            .onFailure {
                println("CLERK: <<< signInWithApple FAILED: ${it.errorMessage}")
                throw Exception(it.errorMessage)
            }
    }
}
