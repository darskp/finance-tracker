package com.finvoraai.personalfinancemanager.finvora.data.repository

import com.finvoraai.personalfinancemanager.finvora.core.network.ApiResult
import com.finvoraai.personalfinancemanager.finvora.core.network.safeApiCall
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.MessageResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import com.finvoraai.personalfinancemanager.finvora.data.remote.DashboardApiService

interface DashboardRepository {
    suspend fun getAllTransactions(): ApiResult<List<TransactionDto>>
    suspend fun getIncome(): ApiResult<List<TransactionDto>>
    suspend fun getExpense(): ApiResult<List<TransactionDto>>
    suspend fun addIncome(request: TransactionRequest): ApiResult<TransactionDto>
    suspend fun updateIncome(id: String, request: TransactionRequest): ApiResult<TransactionDto>
    suspend fun deleteIncome(id: String): ApiResult<MessageResponse>
    suspend fun addExpense(request: TransactionRequest): ApiResult<TransactionDto>
    suspend fun updateExpense(id: String, request: TransactionRequest): ApiResult<TransactionDto>
    suspend fun deleteExpense(id: String): ApiResult<MessageResponse>
}

class DashboardRepositoryImpl(
    private val apiService: DashboardApiService
) : DashboardRepository {
    override suspend fun getAllTransactions() = safeApiCall("getAllTransactions") { apiService.getAllTransactions() }

    override suspend fun getIncome() = safeApiCall("getIncome") { apiService.getIncome() }

    override suspend fun getExpense() = safeApiCall("getExpense") { apiService.getExpense() }

    override suspend fun addIncome(request: TransactionRequest) =
        safeApiCall("addIncome") { apiService.addIncome(request) }

    override suspend fun updateIncome(id: String, request: TransactionRequest) =
        safeApiCall("updateIncome") { apiService.updateIncome(id, request) }

    override suspend fun deleteIncome(id: String) = safeApiCall("deleteIncome") { apiService.deleteIncome(id) }

    override suspend fun addExpense(request: TransactionRequest) =
        safeApiCall("addExpense") { apiService.addExpense(request) }

    override suspend fun updateExpense(id: String, request: TransactionRequest) =
        safeApiCall("updateExpense") { apiService.updateExpense(id, request) }

    override suspend fun deleteExpense(id: String) = safeApiCall("deleteExpense") { apiService.deleteExpense(id) }
}
