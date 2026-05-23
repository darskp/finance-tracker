package com.finvoraai.personalfinancemanager.finvora.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.finvoraai.personalfinancemanager.finvora.core.auth.AuthState
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.OnBoardingViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.RootNavigation
import com.finvoraai.personalfinancemanager.finvora.feature.splash.SplashScreen
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.RootNavGraph
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

private const val SPLASH_TIMEOUT_MS = 5000L

@Composable
fun MainScreen(
    navController: NavHostController,
    isColdStart: Boolean = true,
    authViewModel: AuthViewModel = koinViewModel(),
    onBoardingViewModel: OnBoardingViewModel = koinViewModel()
) {
    val authState by authViewModel.authState.collectAsState()
    val hasCompletedOnboarding by onBoardingViewModel.hasCompletedOnboarding.collectAsState()

    var showSplashAnimation by remember { mutableStateOf(isColdStart) }

    var lockedDestination by remember { mutableStateOf<String?>(null) }
    var initialRoute by remember { mutableStateOf<NavRoute?>(null) }

    LaunchedEffect(authState, hasCompletedOnboarding) {
        if (lockedDestination != null) return@LaunchedEffect
        if (authState is AuthState.Loading || hasCompletedOnboarding == null) return@LaunchedEffect

        when (authState) {
            is AuthState.LoggedIn -> {
                lockedDestination = RootNavGraph.Main.route
                initialRoute = NavRoute.HomeScreen
            }
            is AuthState.LoggedOut -> {
                lockedDestination = RootNavGraph.Onboarding.route
                if (hasCompletedOnboarding == true) {
                    initialRoute = NavRoute.SignInScreen
                } else {
                    initialRoute = NavRoute.WelcomeScreen
                }
            }
            else -> {}
        }
    }

    // Safety timeout: if Clerk never restores session, fall through to onboarding
    LaunchedEffect(Unit) {
        if (isColdStart) {
            delay(SPLASH_TIMEOUT_MS)
            if (lockedDestination == null) {
                lockedDestination = RootNavGraph.Onboarding.route
                initialRoute = NavRoute.WelcomeScreen
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (lockedDestination != null) {
                RootNavigation(
                    navController = navController,
                    startDestination = lockedDestination!!,
                    initialRoute = initialRoute ?: NavRoute.WelcomeScreen
                )
            }

            if (showSplashAnimation || lockedDestination == null) {
                SplashScreen(onComplete = {
                    if (lockedDestination != null) {
                        showSplashAnimation = false
                    }
                })
            }
        }
    }
}
