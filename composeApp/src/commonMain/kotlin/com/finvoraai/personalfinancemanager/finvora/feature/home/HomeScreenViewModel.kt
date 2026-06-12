package com.finvoraai.personalfinancemanager.finvora.feature.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.core.network.ApiResult
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

    private var loadJob: Job? = null

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        if (loadJob?.isActive == true) {
            DebugLogger.network("ViewModel", "loadTransactions() skipped — already loading")
            return
        }
        DebugLogger.network("ViewModel", "loadTransactions() started")
        loadJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.getAllTransactions()) {
                is ApiResult.Success -> {
                    val transactions = result.data
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
                }
                is ApiResult.Error -> {
                    DebugLogger.network("ViewModel", "loadTransactions() failed: ${result.error.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun refresh() {
        if (loadJob?.isActive == true) {
            DebugLogger.network("ViewModel", "refresh() skipped — already loading")
            return
        }
        DebugLogger.network("ViewModel", "refresh() triggered")
        _isRefreshing.value = true
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadJob = viewModelScope.launch {
            when (val result = dashboardRepository.getAllTransactions()) {
                is ApiResult.Success -> {
                    val transactions = result.data
                    DebugLogger.network("ViewModel", "refresh() success: ${transactions.size} transactions")
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
                }
                is ApiResult.Error -> {
                    DebugLogger.network("ViewModel", "refresh() failed: ${result.error.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
            _isRefreshing.value = false
        }
    }

    fun addIncomeEntry(request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.addIncome(request)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun updateIncomeEntry(id: String, request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.updateIncome(id, request)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun deleteIncomeEntry(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.deleteIncome(id)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun addExpenseEntry(request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.addExpense(request)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun updateExpenseEntry(id: String, request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.updateExpense(id, request)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }

    fun deleteExpenseEntry(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = dashboardRepository.deleteExpense(id)) {
                is ApiResult.Success -> loadTransactions()
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.error.message
                    )
                }
            }
        }
    }
}
