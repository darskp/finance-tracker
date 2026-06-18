package com.finvoraai.personalfinancemanager.finvora.data.model.remote

import kotlinx.serialization.Serializable

@Serializable
data class AiChatHistoryResponse(
    val history: List<ChatMessageDto> = emptyList(),
    val activePendingIds: List<String> = emptyList(),
    val hasMore: Boolean = false
)

@Serializable
data class ChatMessageDto(
    val role: String,
    val content: String,
    // Keep as string for parsing ISO 8601
    val createdAt: String,
    val pendingActions: List<PendingActionDto>? = null,
    val resolvedStatus: Map<String, String>? = null
)

@Serializable
data class AiChatRequest(
    val message: String,
    val payload: AiChatPayload? = null
)

@Serializable
data class AiChatPayload(
    val pendingId: String,
    val data: PendingActionData? = null
)

@Serializable
data class PendingActionData(
    val title: String,
    val amount: Double,
    val category: String?,
    val date: String?,
    val emoji: String?
)

@Serializable
data class AiChatResponse(
    val reply: String,
    val pendingActions: List<PendingActionDto>? = null,
    val metadata: AiChatMetadata? = null,
    val success: Boolean = true
)

@Serializable
data class AiChatMetadata(
    val resolvedStatus: Map<String, String>? = null
)

@Serializable
data class PendingActionDto(
    val type: String,
    val title: String? = null,
    val details: String? = null,
    val pendingId: String,
    val amount: Double? = null,
    val emoji: String? = null,
    val date: String? = null,
    val category: String? = null,
    val isAutoLearned: Boolean = false
)
