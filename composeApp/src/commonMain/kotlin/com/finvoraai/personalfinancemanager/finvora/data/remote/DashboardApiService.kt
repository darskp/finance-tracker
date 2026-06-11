package com.finvoraai.personalfinancemanager.finvora.data.remote

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.core.network.BASE_API_URL
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.MessageResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthManager
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import kotlinx.serialization.json.Json

class DashboardApiService(
    private val client: HttpClient,
    private val json: Json,
    private val authManager: AuthManager
) {
    private suspend fun HttpRequestBuilder.withAuth() {
        val token = authManager.getToken()
        if (token != null) {
            DebugLogger.network("Auth Token", "attached (${token.take(20)}...)")
            header(HttpHeaders.Authorization, "Bearer $token")
        } else {
            DebugLogger.network("Auth Token", "null - not attached")
        }
    }

    suspend fun getAllTransactions(): List<TransactionDto> {
        val url = "${BASE_API_URL}get-alltransaction"
        DebugLogger.network("GET get-alltransaction", "URL: $url")
        return try {
            val response = client.get(url) { withAuth() }
            val raw = response.bodyAsText()
            DebugLogger.network("GET get-alltransaction - Raw Response", raw.take(800))
            json.decodeFromString<List<TransactionDto>>(raw)
        } catch (e: Exception) {
            DebugLogger.network("GET get-alltransaction", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun getIncome(): List<TransactionDto> {
        val url = "${BASE_API_URL}get-income"
        DebugLogger.network("GET get-income", "URL: $url")
        return try {
            val response = client.get(url) { withAuth() }
            val raw = response.bodyAsText()
            DebugLogger.network("GET get-income - Raw Response", raw.take(800))
            json.decodeFromString<List<TransactionDto>>(raw)
        } catch (e: Exception) {
            DebugLogger.network("GET get-income", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun getExpense(): List<TransactionDto> {
        val url = "${BASE_API_URL}get-expense"
        DebugLogger.network("GET get-expense", "URL: $url")
        return try {
            val response = client.get(url) { withAuth() }
            val raw = response.bodyAsText()
            DebugLogger.network("GET get-expense - Raw Response", raw.take(800))
            json.decodeFromString<List<TransactionDto>>(raw)
        } catch (e: Exception) {
            DebugLogger.network("GET get-expense", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun addIncome(request: TransactionRequest): TransactionDto {
        val url = "${BASE_API_URL}add-income"
        DebugLogger.network("POST add-income", "URL: $url, Body: ${json.encodeToString(request)}")
        return try {
            val response = client.post(url) { withAuth(); setBody(request) }
            val raw = response.bodyAsText()
            DebugLogger.network("POST add-income - Response", raw.take(800))
            json.decodeFromString<TransactionDto>(raw)
        } catch (e: Exception) {
            DebugLogger.network("POST add-income", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun updateIncome(id: String, request: TransactionRequest): TransactionDto {
        val url = "${BASE_API_URL}update-income/$id"
        DebugLogger.network("PUT update-income", "URL: $url, Body: ${json.encodeToString(request)}")
        return try {
            val response = client.put(url) { withAuth(); setBody(request) }
            val raw = response.bodyAsText()
            DebugLogger.network("PUT update-income - Response", raw.take(800))
            json.decodeFromString<TransactionDto>(raw)
        } catch (e: Exception) {
            DebugLogger.network("PUT update-income", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun deleteIncome(id: String): MessageResponse {
        val url = "${BASE_API_URL}delete-income/$id"
        DebugLogger.network("DELETE delete-income", "URL: $url")
        return try {
            val response = client.delete(url) { withAuth() }
            val raw = response.bodyAsText()
            DebugLogger.network("DELETE delete-income - Response", raw.take(800))
            json.decodeFromString<MessageResponse>(raw)
        } catch (e: Exception) {
            DebugLogger.network("DELETE delete-income", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun addExpense(request: TransactionRequest): TransactionDto {
        val url = "${BASE_API_URL}add-expense"
        DebugLogger.network("POST add-expense", "URL: $url, Body: ${json.encodeToString(request)}")
        return try {
            val response = client.post(url) { withAuth(); setBody(request) }
            val raw = response.bodyAsText()
            DebugLogger.network("POST add-expense - Response", raw.take(800))
            json.decodeFromString<TransactionDto>(raw)
        } catch (e: Exception) {
            DebugLogger.network("POST add-expense", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun updateExpense(id: String, request: TransactionRequest): TransactionDto {
        val url = "${BASE_API_URL}update-expense/$id"
        DebugLogger.network("PUT update-expense", "URL: $url, Body: ${json.encodeToString(request)}")
        return try {
            val response = client.put(url) { withAuth(); setBody(request) }
            val raw = response.bodyAsText()
            DebugLogger.network("PUT update-expense - Response", raw.take(800))
            json.decodeFromString<TransactionDto>(raw)
        } catch (e: Exception) {
            DebugLogger.network("PUT update-expense", "Error: ${e.message}")
            throw e
        }
    }

    suspend fun deleteExpense(id: String): MessageResponse {
        val url = "${BASE_API_URL}delete-expense/$id"
        DebugLogger.network("DELETE delete-expense", "URL: $url")
        return try {
            val response = client.delete(url) { withAuth() }
            val raw = response.bodyAsText()
            DebugLogger.network("DELETE delete-expense - Response", raw.take(800))
            json.decodeFromString<MessageResponse>(raw)
        } catch (e: Exception) {
            DebugLogger.network("DELETE delete-expense", "Error: ${e.message}")
            throw e
        }
    }
}
