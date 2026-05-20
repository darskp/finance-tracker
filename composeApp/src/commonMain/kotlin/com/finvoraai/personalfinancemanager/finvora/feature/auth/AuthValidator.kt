package com.finvoraai.personalfinancemanager.finvora.feature.auth

sealed interface ValidationResult {
    object Success : ValidationResult
    data class Failure(val message: String) : ValidationResult
}

private const val MIN_PASSWORD_LENGTH = 8
private const val OTP_CODE_LENGTH = 6

object AuthValidator {
    fun validateEmail(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult.Failure("Email cannot be blank.")
        }
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return ValidationResult.Failure("Enter a valid email address.")
        }
        return ValidationResult.Success
    }

    fun validatePassword(password: String): ValidationResult {
        if (password.length < MIN_PASSWORD_LENGTH) {
            return ValidationResult.Failure("Password must be at least $MIN_PASSWORD_LENGTH characters.")
        }
        return ValidationResult.Success
    }

    fun validateConfirmPassword(password: String, confirm: String): ValidationResult {
        if (password != confirm) {
            return ValidationResult.Failure("Passwords do not match.")
        }
        return ValidationResult.Success
    }

    fun validateOtp(code: String): ValidationResult {
        if (code.isBlank()) {
            return ValidationResult.Failure("Verification code cannot be blank.")
        }
        if (code.length != OTP_CODE_LENGTH || !code.all { it.isDigit() }) {
            return ValidationResult.Failure("Enter a valid $OTP_CODE_LENGTH-digit verification code.")
        }
        return ValidationResult.Success
    }
}
