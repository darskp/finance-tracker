package com.finvoraai.personalfinancemanager.finvora.core.network

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode

suspend fun <T> safeApiCall(
    tag: String = "API",
    block: suspend () -> T
): ApiResult<T> {
    DebugLogger.network(tag, "starting")
    return try {
        val result = block()
        DebugLogger.network(tag, "success")
        ApiResult.Success(result)
    } catch (e: ClientRequestException) {
        val status = e.response.status
        DebugLogger.network(tag, "HTTP ${status.value}: ${e.message}")
        when (status) {
            HttpStatusCode.Unauthorized -> {
                SessionManager.onUnauthorized()
                ApiResult.Error(AppError.Unauthorized())
            }
            HttpStatusCode.Forbidden -> ApiResult.Error(AppError.Forbidden())
            HttpStatusCode.NotFound -> ApiResult.Error(AppError.NotFound())
            HttpStatusCode.TooManyRequests -> ApiResult.Error(AppError.RateLimited())
            else -> {
                if (status.value >= 500) {
                    ApiResult.Error(AppError.ServerError())
                } else {
                    ApiResult.Error(AppError.Unknown("HTTP ${status.value}", e))
                }
            }
        }
    } catch (e: HttpRequestTimeoutException) {
        DebugLogger.network(tag, "timeout: ${e.message}")
        ApiResult.Error(AppError.NetworkTimeout())
    } catch (e: Exception) {
        DebugLogger.network(tag, "error: ${e.message}")
        val message = e.message ?: "Unexpected error"
        when {
            message.contains("timed out", ignoreCase = true) ||
                message.contains("timeout", ignoreCase = true) ->
                ApiResult.Error(AppError.NetworkTimeout())
            message.contains("resolve", ignoreCase = true) ||
                message.contains("network", ignoreCase = true) ||
                message.contains("connection", ignoreCase = true) ->
                ApiResult.Error(AppError.NoInternet())
            else -> ApiResult.Error(AppError.Unknown(message, e))
        }
    }
}
