package com.finvoraai.personalfinancemanager.finvora.feature.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.OnBoardingViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.RootNavigation
import com.finvoraai.personalfinancemanager.finvora.feature.splash.SplashScreen
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.RootNavGraph
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    navController: NavHostController,
    initialRoute: NavRoute? = null,
    isColdStart: Boolean = true,
    onboardingViewModel: OnBoardingViewModel = koinViewModel()
) {
    var resolvedRoute by remember { mutableStateOf(initialRoute) }

    LaunchedEffect(Unit) {
        if (resolvedRoute == null) {
            resolvedRoute = onboardingViewModel.getCurrentRoute()
        }
    }

    val currentRoute = resolvedRoute
    var showSplashAnimation by remember { mutableStateOf(isColdStart) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (currentRoute != null) {
                val startDestination = remember(currentRoute) {
                    when (currentRoute) {
                        NavRoute.Onboarding,
                        NavRoute.WelcomeScreen,
                        NavRoute.SignInScreen,
                        NavRoute.SignUpScreen
                        -> RootNavGraph.Onboarding.route
                        else -> RootNavGraph.Main.route
                    }
                }
                RootNavigation(
                    navController = navController,
                    startDestination = startDestination,
                    initialRoute = currentRoute
                )
            }

            if (showSplashAnimation || currentRoute == null) {
                SplashScreen(onComplete = {
                    if (currentRoute != null) {
                        showSplashAnimation = false
                    }
                })
            }
        }
    }
}
