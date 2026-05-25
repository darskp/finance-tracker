package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.data.model.ThemeSetting
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Immutable
data class ChatUiState(
    val themeSetting: ThemeSetting? = null,
    val isLoading: Boolean = true
)

class ChatViewModel(
    private val settingsDao: SettingsDao
) : ViewModel() {

    val uiState: StateFlow<ChatUiState> = settingsDao.getThemeSetting()
        .map { themeSetting ->
            ChatUiState(themeSetting = themeSetting, isLoading = false)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatUiState()
        )
}
