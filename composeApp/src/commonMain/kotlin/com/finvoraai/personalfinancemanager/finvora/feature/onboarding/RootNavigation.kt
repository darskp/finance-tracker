package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.MainNavigation
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.RootNavGraph

@Composable
fun RootNavigation(
    navController: NavHostController,
    startDestination: String,
    initialRoute: NavRoute = NavRoute.WelcomeScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Onboarding Navigation Graph
        composable(RootNavGraph.Onboarding.route) {
            val onboardingNavController = rememberNavController()
            OnboardingNavigation(
                navController = onboardingNavController,
                startDestination = initialRoute,
                onAuthComplete = {
                    navController.navigate(RootNavGraph.Main.route) {
                        popUpTo(RootNavGraph.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // Main App Navigation Graph
        composable(RootNavGraph.Main.route) {
            val mainNavController = rememberNavController()
            MainNavigation(navController = mainNavController)
        }
    }
}
