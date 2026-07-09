package com.finvoraai.personalfinancemanager.finvora.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val _id: String,
    val userId: String?,
    val transactionType: String,
    val title: String,
    val emoji: String,
    val category: String,
    val amount: String,
    val date: String,
    val createdAt: String?,
    val resolveId: String?
)

fun TransactionEntity.toDto() = TransactionDto(
    _id = _id,
    userId = userId,
    transactionType = TransactionType.valueOf(transactionType),
    title = title,
    emoji = emoji,
    category = category,
    amount = amount,
    date = date,
    createdAt = createdAt,
    resolveId = resolveId
)

fun TransactionDto.toEntity() = TransactionEntity(
    // Assuming ID is present for cached items, we need a PK
    _id = _id ?: "",
    userId = userId,
    transactionType = transactionType.name,
    title = title,
    emoji = emoji,
    category = category,
    amount = amount,
    date = date,
    createdAt = createdAt,
    resolveId = resolveId
)
