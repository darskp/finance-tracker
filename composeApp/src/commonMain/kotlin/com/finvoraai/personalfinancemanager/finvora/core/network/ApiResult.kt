package com.finvoraai.personalfinancemanager.finvora.core.network

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: AppError) : ApiResult<Nothing>()
}

sealed class AppError(open val message: String, open val cause: Throwable? = null) {
    data class Unauthorized(
        override val message: String = "Session expired. Please sign in again."
    ) : AppError(message)

    data class Forbidden(
        override val message: String = "Access denied."
    ) : AppError(message)

    data class NotFound(
        override val message: String = "Resource not found."
    ) : AppError(message)

    data class RateLimited(
        override val message: String = "Too many requests. Please try again later."
    ) : AppError(message)

    data class ServerError(
        override val message: String = "Server error. Please try again later."
    ) : AppError(message)

    data class NetworkTimeout(
        override val message: String = "Request timed out. Please check your connection."
    ) : AppError(message)

    data class NoInternet(
        override val message: String = "No internet connection."
    ) : AppError(message)

    data class Unknown(
        override val message: String,
        override val cause: Throwable?
    ) : AppError(message, cause)
}
