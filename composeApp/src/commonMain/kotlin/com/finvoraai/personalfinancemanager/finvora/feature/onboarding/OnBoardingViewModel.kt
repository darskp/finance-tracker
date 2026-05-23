package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val HAS_COMPLETED_ONBOARDING_PREF_KEY = "has_completed_onboarding"

class OnBoardingViewModel(
    val prefs: DataStore<Preferences>
) : ViewModel() {

    private val hasCompletedOnboardingKey = booleanPreferencesKey(HAS_COMPLETED_ONBOARDING_PREF_KEY)

    val hasCompletedOnboarding = prefs.data.map { it[hasCompletedOnboardingKey] ?: false }
        .onEach { DebugLogger.onboarding("hasCompletedOnboarding", it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setOnboardingCompleted(isCompleted: Boolean) {
        viewModelScope.launch {
            prefs.edit { it[hasCompletedOnboardingKey] = isCompleted }
        }
    }
}
