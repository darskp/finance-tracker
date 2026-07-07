package com.finvoraai.personalfinancemanager.finvora.data.remote

import com.finvoraai.personalfinancemanager.finvora.data.model.remote.MessageResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class DashboardApiService(
    private val client: HttpClient
) {
    suspend fun getAllTransactions(): List<TransactionDto> = client.get("get-alltransaction").body()

    suspend fun getIncome(): List<TransactionDto> = client.get("get-income").body()

    suspend fun getExpense(): List<TransactionDto> = client.get("get-expense").body()

    suspend fun addIncome(request: TransactionRequest): TransactionDto =
        client.post("add-income") { setBody(request) }.body()

    suspend fun updateIncome(id: String, request: TransactionRequest): TransactionDto =
        client.put("update-income/$id") { setBody(request) }.body()

    suspend fun deleteIncome(id: String): MessageResponse = client.delete("delete-income/$id").body()

    suspend fun addExpense(request: TransactionRequest): TransactionDto =
        client.post("add-expenses") { setBody(request) }.body()

    suspend fun updateExpense(id: String, request: TransactionRequest): TransactionDto =
        client.put("update-expenses/$id") { setBody(request) }.body()

    suspend fun deleteExpense(id: String): MessageResponse = client.delete("delete-expenses/$id").body()
}
