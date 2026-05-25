package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.data.model.ThemeSetting
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.theme_dark
import finvoraai.composeapp.generated.resources.theme_light
import finvoraai.composeapp.generated.resources.theme_ocean
import finvoraai.composeapp.generated.resources.theme_system
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

@Immutable
data class ChatUiState(
    val themeSetting: ThemeSetting? = null,
    val currentThemeLabelRes: StringResource = Res.string.theme_system,
    val currentThemeKey: String = "System",
    val isLoading: Boolean = true
)

class ChatViewModel(
    private val settingsDao: SettingsDao
) : ViewModel() {

    val uiState: StateFlow<ChatUiState> = settingsDao.getThemeSetting()
        .map { themeSetting ->
            val themeStr = themeSetting?.theme ?: "System"
            val labelRes = when (themeStr.lowercase()) {
                "dark" -> Res.string.theme_dark
                "light" -> Res.string.theme_light
                "ocean" -> Res.string.theme_ocean
                else -> Res.string.theme_system
            }
            ChatUiState(
                themeSetting = themeSetting,
                currentThemeLabelRes = labelRes,
                currentThemeKey = themeStr,
                isLoading = false
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ChatUiState()
        )

    fun updateTheme(newTheme: String) {
        viewModelScope.launch {
            settingsDao.saveThemeSetting(ThemeSetting(theme = newTheme))
        }
    }
}
