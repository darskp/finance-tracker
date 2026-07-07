package com.finvoraai.personalfinancemanager.finvora.core.network

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_API_URL = "https://expense-tracker-backend-eight-sandy.vercel.app/api/"

fun createHttpClient(authManager: AuthManager, json: Json): HttpClient {
    DebugLogger.network("API Base URL", BASE_API_URL)
    return HttpClient {
        expectSuccess = true

        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Ktor: $message")
                }
            }
            level = LogLevel.ALL
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val token = authManager.getToken()
                    if (token != null) {
                        DebugLogger.network("Auth", "Bearer token attached (${token.take(20)}...)")
                        BearerTokens(accessToken = token, refreshToken = token)
                    } else {
                        DebugLogger.network("Auth", "no token available")
                        null
                    }
                }
                refreshTokens {
                    // Called by Ktor when a 401 is received — Clerk auto-refreshes the
                    // session in the background, so calling getToken() here gets a fresh JWT
                    // without forcing the user to log in again.
                    val freshToken = authManager.getToken()
                    if (freshToken != null) {
                        DebugLogger.network("Auth", "Token refreshed (${freshToken.take(20)}...)")
                        BearerTokens(accessToken = freshToken, refreshToken = freshToken)
                    } else {
                        DebugLogger.network("Auth", "Token refresh failed — no token")
                        null
                    }
                }
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            url(BASE_API_URL)
        }
    }
}
