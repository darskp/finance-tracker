package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import kotlinx.coroutines.flow.first

private const val IS_NEW_USER_PREF_KEY = "is_new_user"

class OnBoardingViewModel(
    val prefs: DataStore<Preferences>
) : ViewModel() {

    private val isNewUserKey = booleanPreferencesKey(IS_NEW_USER_PREF_KEY)

    suspend fun getCurrentRoute(): NavRoute {
        val isNewUser = prefs.data.first()[isNewUserKey] ?: true
        if (isNewUser) {
            return NavRoute.WelcomeScreen
        }

        return NavRoute.HomeScreen
    }

    suspend fun setUserStatus(isFirstTime: Boolean): Boolean {
        prefs.edit { it[isNewUserKey] = isFirstTime }
        return true
    }
}
