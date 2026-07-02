package com.finvoraai.personalfinancemanager.finvora.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.data.model.ThemeSetting
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthManager
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.theme_dark
import finvoraai.composeapp.generated.resources.theme_light
import finvoraai.composeapp.generated.resources.theme_ocean
import finvoraai.composeapp.generated.resources.theme_system
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource

data class ProfileUiState(
    val currentThemeKey: String = "System",
    val currentThemeLabelRes: StringResource = Res.string.theme_system,
    val email: String? = null,
    val name: String? = null,
    val isLoading: Boolean = true
)

class ProfileViewModel(
    private val settingsDao: SettingsDao,
    private val authManager: AuthManager
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> = combine(
        settingsDao.getThemeSetting(),
        authManager.observeUser()
    ) { themeSetting, authUser ->
        val themeStr = themeSetting?.theme ?: "System"
        val labelRes = when (themeStr.lowercase()) {
            "dark" -> Res.string.theme_dark
            "light" -> Res.string.theme_light
            "ocean" -> Res.string.theme_ocean
            else -> Res.string.theme_system
        }
        
        val email = authUser?.email
        val name = email?.substringBefore("@")
            ?.replace(".", " ")
            ?.split(" ")
            ?.joinToString(" ") { word -> word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() } }
            
        ProfileUiState(
            currentThemeKey = themeStr,
            currentThemeLabelRes = labelRes,
            email = email,
            name = name,
            isLoading = false
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ProfileUiState()
        )

    fun updateTheme(newTheme: String) {
        viewModelScope.launch {
            settingsDao.saveThemeSetting(ThemeSetting(theme = newTheme))
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                authManager.signOut()
                onSignOutComplete()
            } catch (e: Exception) {
                // handle error if needed
                onSignOutComplete()
            }
        }
    }
}
