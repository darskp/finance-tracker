package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.data.model.AuthSetting
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OnBoardingViewModel(
    private val settingsDao: SettingsDao
) : ViewModel() {

    val hasCompletedOnboarding = settingsDao.getAuthSetting().map { it?.hasCompletedOnboarding ?: false }
        .onEach { DebugLogger.onboarding("hasCompletedOnboarding", it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setOnboardingCompleted(isCompleted: Boolean) {
        viewModelScope.launch {
            settingsDao.saveAuthSetting(AuthSetting(hasCompletedOnboarding = isCompleted))
        }
    }
}
