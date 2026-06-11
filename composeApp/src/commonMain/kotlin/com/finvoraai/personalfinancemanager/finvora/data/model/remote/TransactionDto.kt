package com.finvoraai.personalfinancemanager.finvora.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType {
    Income, Expense
}

@Serializable
data class TransactionDto(
    val _id: String? = null,
    val userId: String? = null,
    val transactionType: TransactionType,
    val title: String,
    val emoji: String,
    val category: String,
    val amount: String,
    val date: String,
    val createdAt: String? = null,
    val resolveId: String? = null
)

@Serializable
data class TransactionRequest(
    val transactionType: TransactionType,
    val title: String,
    val emoji: String,
    val category: String,
    val amount: String,
    val date: String
)

@Serializable
data class MessageResponse(
    val message: String
)
