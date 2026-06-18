package com.finvoraai.personalfinancemanager.finvora.data.remote

import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatHistoryResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatRequest
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.AiChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ChatApiService(private val client: HttpClient) {

    suspend fun getSuggestions(): List<String> {
        return client.get("ai-chat/suggestions").body()
    }

    suspend fun getHistory(skip: Int, limit: Int): AiChatHistoryResponse {
        return client.get("ai-chat/history") {
            url {
                parameters.append("skip", skip.toString())
                parameters.append("limit", limit.toString())
            }
        }.body()
    }

    suspend fun sendMessage(request: AiChatRequest): AiChatResponse {
        return client.post("ai-chat") {
            setBody(request)
        }.body()
    }
}
