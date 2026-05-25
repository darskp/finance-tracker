package com.finvoraai.personalfinancemanager.finvora.feature.main

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.finvoraai.personalfinancemanager.finvora.core.auth.AuthState
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthManager
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.IconSource
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.NavItem
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.home
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val HAS_COMPLETED_ONBOARDING_PREF_KEY = "has_completed_onboarding"
private const val KEY_STARTUP_DESTINATION = "startupDestination"

sealed interface StartupDestination {
    data object Loading : StartupDestination
    data class Onboarding(val initialRoute: NavRoute) : StartupDestination
    data object Main : StartupDestination
}

class MainViewModel(
    private val authManager: AuthManager,
    private val prefs: DataStore<Preferences>
) : ViewModel() {

    private val hasCompletedOnboardingKey = booleanPreferencesKey(HAS_COMPLETED_ONBOARDING_PREF_KEY)

    val bottomNavItems = listOf(
        NavItem(
            icon = IconSource.Drawable(Res.drawable.home),
            label = "Home",
            route = NavRoute.HomeScreen
        )
    )

    // Flow representing onboarding completion state
    val hasCompletedOnboarding = prefs.data.map { it[hasCompletedOnboardingKey] ?: false }

    // Combine Auth State and Onboarding State into a resolved startup state
    val startupDestination: StateFlow<StartupDestination> = combine(
        authManager.observeIsInitialized(),
        authManager.observeUser(),
        hasCompletedOnboarding
    ) { initialized, user, completedOnboarding ->
        // Log Auth info so it is always present in the Debug overlay
        val authState = when {
            !initialized -> AuthState.Loading
            user == null -> AuthState.LoggedOut
            else -> AuthState.LoggedIn(user.id, user.email)
        }
        DebugLogger.auth("authState", authState)
        DebugLogger.auth("userId", user?.id)
        DebugLogger.auth("userEmail", user?.email)
        DebugLogger.auth("clerkInitialized", initialized)

        val destination = if (!initialized) {
            StartupDestination.Loading
        } else {
            if (user != null) {
                StartupDestination.Main
            } else {
                val targetRoute = if (completedOnboarding) {
                    NavRoute.SignInScreen
                } else {
                    NavRoute.WelcomeScreen
                }
                StartupDestination.Onboarding(targetRoute)
            }
        }
        DebugLogger.navigation(KEY_STARTUP_DESTINATION, destination.toString())
        destination
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = StartupDestination.Loading
    )

    fun shouldShowBottomBar(currentRoute: String?): Boolean {
        if (currentRoute == null) return false

        val hideBottomBarRoutes = listOf<String?>(
            // NavRoute.AboutUs::class.simpleName
        )

        // Check if current route matches any of the hidden routes
        return hideBottomBarRoutes.none { hiddenRoute ->
            hiddenRoute != null && currentRoute.contains(hiddenRoute)
        }
    }
}

