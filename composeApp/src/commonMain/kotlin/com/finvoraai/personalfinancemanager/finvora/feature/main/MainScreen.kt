package com.finvoraai.personalfinancemanager.finvora.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugBuildCheck
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugFloatingButton
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugOverlay
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.RootNavigation
import com.finvoraai.personalfinancemanager.finvora.feature.splash.SplashScreen
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.RootNavGraph
import org.koin.compose.viewmodel.koinViewModel

private const val KEY_LOCKED_DESTINATION = "lockedDestination"
private const val KEY_INITIAL_ROUTE = "initialRoute"
private const val KEY_STATE = "state"
private const val KEY_TRANSITION = "transition"

@Composable
fun MainScreen(
    navController: NavHostController,
    isColdStart: Boolean = true,
    mainViewModel: MainViewModel = koinViewModel()
) {
    val startupDestination by mainViewModel.startupDestination.collectAsState()

    var showSplashAnimation by remember { mutableStateOf(isColdStart) }

    var lockedDestination by remember { mutableStateOf<String?>(null) }
    var initialRoute by remember { mutableStateOf<NavRoute?>(null) }

    LaunchedEffect(startupDestination) {
        DebugLogger.navigation("startupDestinationChanged", startupDestination.toString())
        when (val destination = startupDestination) {
            is StartupDestination.Loading -> {
                DebugLogger.navigation(KEY_STATE, "Loading - Showing Splash")
            }
            is StartupDestination.Onboarding -> {
                val targetRoute = destination.initialRoute
                DebugLogger.navigation(KEY_STATE, "Onboarding - Target: $targetRoute")
                if (lockedDestination == null) {
                    // Cold start: set initial destination
                    lockedDestination = RootNavGraph.Onboarding.route
                    initialRoute = targetRoute
                    DebugLogger.navigation(KEY_LOCKED_DESTINATION, lockedDestination)
                    DebugLogger.navigation(KEY_INITIAL_ROUTE, initialRoute)
                } else if (lockedDestination == RootNavGraph.Main.route) {
                    // Logout transition (cold started on Main):
                    // Navigate to Onboarding, keeping Main at the bottom of the stack
                    DebugLogger.navigation(KEY_TRANSITION, "LOGOUT -> Navigate to Onboarding")
                    initialRoute = targetRoute
                    DebugLogger.navigation(KEY_INITIAL_ROUTE, initialRoute)
                    navController.navigate(RootNavGraph.Onboarding.route) {
                        popUpTo(RootNavGraph.Main.route) { inclusive = false }
                    }
                } else {
                    // Logout transition (cold started on Onboarding):
                    // Pop back to the Onboarding graph that is at the bottom of the stack
                    DebugLogger.navigation(KEY_TRANSITION, "LOGOUT -> Pop back to Onboarding")
                    initialRoute = targetRoute
                    DebugLogger.navigation(KEY_INITIAL_ROUTE, initialRoute)
                    navController.popBackStack(RootNavGraph.Onboarding.route, inclusive = false)
                }
            }
            is StartupDestination.Main -> {
                DebugLogger.navigation(KEY_STATE, "Main - Authenticated Graph")
                if (lockedDestination == null) {
                    // Cold start: set initial destination
                    lockedDestination = RootNavGraph.Main.route
                    initialRoute = NavRoute.HomeScreen
                    DebugLogger.navigation(KEY_LOCKED_DESTINATION, lockedDestination)
                    DebugLogger.navigation(KEY_INITIAL_ROUTE, initialRoute)
                } else if (lockedDestination == RootNavGraph.Onboarding.route) {
                    // Login transition (cold started on Onboarding):
                    // Navigate to Main, keeping Onboarding at the bottom of the stack
                    DebugLogger.navigation(KEY_TRANSITION, "LOGIN -> Navigate to Main")
                    initialRoute = NavRoute.HomeScreen
                    navController.navigate(RootNavGraph.Main.route) {
                        popUpTo(RootNavGraph.Onboarding.route) { inclusive = false }
                    }
                } else {
                    // Login transition (cold started on Main):
                    // Pop back to the Main graph that is at the bottom of the stack
                    DebugLogger.navigation(KEY_TRANSITION, "LOGIN -> Pop back to Main")
                    initialRoute = NavRoute.HomeScreen
                    navController.popBackStack(RootNavGraph.Main.route, inclusive = false)
                }
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
                    showSplashAnimation = false
                })
            }

            if (DebugBuildCheck.isDebug()) {
                DebugOverlay()
                DebugFloatingButton(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                )
            }
        }
    }
}
