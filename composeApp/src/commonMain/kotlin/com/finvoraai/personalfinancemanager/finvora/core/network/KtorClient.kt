package com.finvoraai.personalfinancemanager.finvora.core.network

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import io.ktor.client.HttpClient
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

fun createHttpClient(json: Json): HttpClient {
    DebugLogger.network("API Base URL", BASE_API_URL)
    return HttpClient {
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

        defaultRequest {
            contentType(ContentType.Application.Json)
            url(BASE_API_URL)
        }
    }
}
