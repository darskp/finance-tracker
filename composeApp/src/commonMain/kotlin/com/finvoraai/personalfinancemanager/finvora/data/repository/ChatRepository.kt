package com.finvoraai.personalfinancemanager.finvora.data.repository

import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatHistoryResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatRequest
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatResponse
import com.finvoraai.personalfinancemanager.finvora.data.remote.ChatApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepository(private val apiService: ChatApiService) {

    fun getSuggestions(): Flow<Result<List<String>>> = flow {
        try {
            val suggestions = apiService.getSuggestions()
            emit(Result.success(suggestions))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun getHistory(skip: Int, limit: Int): Flow<Result<AiChatHistoryResponse>> = flow {
        try {
            val response = apiService.getHistory(skip, limit)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    fun sendMessage(request: AiChatRequest): Flow<Result<AiChatResponse>> = flow {
        try {
            val response = apiService.sendMessage(request)
            emit(Result.success(response))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
