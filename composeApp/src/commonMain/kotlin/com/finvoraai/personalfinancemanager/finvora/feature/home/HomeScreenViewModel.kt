package com.finvoraai.personalfinancemanager.finvora.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionDto
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionType
import com.finvoraai.personalfinancemanager.finvora.data.repository.DashboardRepository

@Immutable
data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionDto> = emptyList(),
    val totalBalance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val totalTransaction: Double = 0.0,
    val error: String? = null
)

class HomeScreenViewModel(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState(isLoading = true))
    val uiState: StateFlow<HomeScreenUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        DebugLogger.network("ViewModel", "loadTransactions() started")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.getAllTransactions()
            result.onSuccess { transactions ->
                DebugLogger.network("ViewModel", "loadTransactions() success: ${transactions.size} transactions")
                val income = transactions.filter { it.transactionType == TransactionType.Income }
                    .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
                val expense = transactions.filter { it.transactionType == TransactionType.Expense }
                    .sumOf { it.amount.toDoubleOrNull() ?: 0.0 }

                val totalTx = income + expense

                DebugLogger.generic("Dashboard", "totalIncome", income)
                DebugLogger.generic("Dashboard", "totalExpense", expense)
                DebugLogger.generic("Dashboard", "totalBalance", income - expense)
                DebugLogger.generic("Dashboard", "totalTransaction", totalTx)

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    transactions = transactions,
                    totalIncome = income,
                    totalExpense = expense,
                    totalBalance = income - expense,
                    totalTransaction = totalTx,
                    error = null
                )
            }.onFailure { error ->
                DebugLogger.network("ViewModel", "loadTransactions() failed: ${error.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to load transactions"
                )
            }
        }
    }

    fun refresh() {
        DebugLogger.network("ViewModel", "refresh() triggered")
        viewModelScope.launch {
            _isRefreshing.value = true
            loadTransactions()
            _isRefreshing.value = false
        }
    }

    fun addIncomeEntry(request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.addIncome(request)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to add income"
                )
            }
        }
    }

    fun updateIncomeEntry(id: String, request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.updateIncome(id, request)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to update income"
                )
            }
        }
    }

    fun deleteIncomeEntry(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.deleteIncome(id)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to delete income"
                )
            }
        }
    }

    fun addExpenseEntry(request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.addExpense(request)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to add expense"
                )
            }
        }
    }

    fun updateExpenseEntry(id: String, request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.updateExpense(id, request)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to update expense"
                )
            }
        }
    }

    fun deleteExpenseEntry(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = dashboardRepository.deleteExpense(id)
            result.onSuccess {
                loadTransactions()
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Failed to delete expense"
                )
            }
        }
    }
}
