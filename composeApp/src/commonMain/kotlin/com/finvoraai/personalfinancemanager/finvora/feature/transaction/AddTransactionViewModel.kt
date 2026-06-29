package com.finvoraai.personalfinancemanager.finvora.feature.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.core.network.ApiResult
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionRequest
import com.finvoraai.personalfinancemanager.finvora.data.model.remote.TransactionType
import com.finvoraai.personalfinancemanager.finvora.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

data class AddTransactionSubmitState(
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AddTransactionViewModel(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _submitState = MutableStateFlow(AddTransactionSubmitState())
    val submitState: StateFlow<AddTransactionSubmitState> = _submitState.asStateFlow()

    fun saveTransaction(
        isExpense: Boolean,
        amount: String,
        title: String,
        category: String,
        emoji: String,
        date: String
    ) {
        if (amount.isBlank() || title.isBlank()) {
            _submitState.value = AddTransactionSubmitState(error = "Please fill all required fields")
            return
        }

        val type = if (isExpense) TransactionType.Expense else TransactionType.Income
        
        // Handle "Today" to an ISO 8601 string, or use the actual date if provided
        val isoDate = if (date.equals("Today", ignoreCase = true) || date.isBlank()) {
            Clock.System.now().toString()
        } else {
            date
        }

        val request = TransactionRequest(
            transactionType = type,
            title = title,
            emoji = emoji,
            category = category,
            amount = amount,
            date = isoDate
        )

        viewModelScope.launch {
            _submitState.value = AddTransactionSubmitState(isLoading = true)
            
            val result = if (isExpense) {
                dashboardRepository.addExpense(request)
            } else {
                dashboardRepository.addIncome(request)
            }

            when (result) {
                is ApiResult.Success -> {
                    _submitState.value = AddTransactionSubmitState(success = true)
                }
                is ApiResult.Error -> {
                    _submitState.value = AddTransactionSubmitState(error = result.error.message)
                }
            }
        }
    }

    fun consumeError() {
        _submitState.value = _submitState.value.copy(error = null)
    }

    fun consumeSuccess() {
        _submitState.value = _submitState.value.copy(success = false)
    }
}
