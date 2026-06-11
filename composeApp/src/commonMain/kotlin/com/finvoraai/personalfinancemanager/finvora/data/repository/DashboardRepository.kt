package com.finvoraai.personalfinancemanager.finvora.data.repository

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.MessageResponse
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import com.finvoraai.personalfinancemanager.finvora.data.remote.DashboardApiService

interface DashboardRepository {
    suspend fun getAllTransactions(): Result<List<TransactionDto>>
    suspend fun getIncome(): Result<List<TransactionDto>>
    suspend fun getExpense(): Result<List<TransactionDto>>
    suspend fun addIncome(request: TransactionRequest): Result<TransactionDto>
    suspend fun updateIncome(id: String, request: TransactionRequest): Result<TransactionDto>
    suspend fun deleteIncome(id: String): Result<MessageResponse>
    suspend fun addExpense(request: TransactionRequest): Result<TransactionDto>
    suspend fun updateExpense(id: String, request: TransactionRequest): Result<TransactionDto>
    suspend fun deleteExpense(id: String): Result<MessageResponse>
}

class DashboardRepositoryImpl(
    private val apiService: DashboardApiService
) : DashboardRepository {

    override suspend fun getAllTransactions(): Result<List<TransactionDto>> {
        DebugLogger.network("Repository", "getAllTransactions() called")
        return try {
            val data = apiService.getAllTransactions()
            DebugLogger.network("Repository", "getAllTransactions() succeeded: ${data.size} items")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "getAllTransactions() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getIncome(): Result<List<TransactionDto>> {
        DebugLogger.network("Repository", "getIncome() called")
        return try {
            val data = apiService.getIncome()
            DebugLogger.network("Repository", "getIncome() succeeded: ${data.size} items")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "getIncome() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getExpense(): Result<List<TransactionDto>> {
        DebugLogger.network("Repository", "getExpense() called")
        return try {
            val data = apiService.getExpense()
            DebugLogger.network("Repository", "getExpense() succeeded: ${data.size} items")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "getExpense() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun addIncome(request: TransactionRequest): Result<TransactionDto> {
        DebugLogger.network("Repository", "addIncome() called: $request")
        return try {
            val data = apiService.addIncome(request)
            DebugLogger.network("Repository", "addIncome() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "addIncome() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateIncome(id: String, request: TransactionRequest): Result<TransactionDto> {
        DebugLogger.network("Repository", "updateIncome() called: id=$id, $request")
        return try {
            val data = apiService.updateIncome(id, request)
            DebugLogger.network("Repository", "updateIncome() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "updateIncome() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteIncome(id: String): Result<MessageResponse> {
        DebugLogger.network("Repository", "deleteIncome() called: id=$id")
        return try {
            val data = apiService.deleteIncome(id)
            DebugLogger.network("Repository", "deleteIncome() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "deleteIncome() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun addExpense(request: TransactionRequest): Result<TransactionDto> {
        DebugLogger.network("Repository", "addExpense() called: $request")
        return try {
            val data = apiService.addExpense(request)
            DebugLogger.network("Repository", "addExpense() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "addExpense() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateExpense(id: String, request: TransactionRequest): Result<TransactionDto> {
        DebugLogger.network("Repository", "updateExpense() called: id=$id, $request")
        return try {
            val data = apiService.updateExpense(id, request)
            DebugLogger.network("Repository", "updateExpense() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "updateExpense() failed: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteExpense(id: String): Result<MessageResponse> {
        DebugLogger.network("Repository", "deleteExpense() called: id=$id")
        return try {
            val data = apiService.deleteExpense(id)
            DebugLogger.network("Repository", "deleteExpense() succeeded: $data")
            Result.success(data)
        } catch (e: Exception) {
            DebugLogger.network("Repository", "deleteExpense() failed: ${e.message}")
            Result.failure(e)
        }
    }
}
