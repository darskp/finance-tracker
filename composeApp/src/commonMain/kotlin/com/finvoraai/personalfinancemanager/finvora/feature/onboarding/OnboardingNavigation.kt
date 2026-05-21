package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.finvoraai.personalfinancemanager.finvora.feature.auth.ForgotPasswordScreen
import com.finvoraai.personalfinancemanager.finvora.feature.auth.SignInScreen
import com.finvoraai.personalfinancemanager.finvora.feature.auth.SignUpScreen
import com.finvoraai.personalfinancemanager.finvora.feature.welcome.WelcomeScreen
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute

/**
 * Onboarding Navigation Graph
 * Handles all onboarding related screens
 */
@Composable
fun OnboardingNavigation(
    navController: NavHostController,
    startDestination: NavRoute = NavRoute.WelcomeScreen,
    onAuthComplete: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<NavRoute.WelcomeScreen> {
            WelcomeScreen(
                onGetStartedClick = { navController.navigate(NavRoute.Onboarding) },
                onViewDemoClick = { /* Action */ }
            )
        }

        composable<NavRoute.Onboarding> {
            OnboardingScreen(
                onOnboardingComplete = { navController.navigate(NavRoute.SignUpScreen) }
            )
        }

        composable<NavRoute.SignUpScreen> {
            SignUpScreen(
                onSignUpSuccess = onAuthComplete,
                onNavigateToSignIn = {
                    navController.navigate(NavRoute.SignInScreen) {
                        popUpTo(NavRoute.SignUpScreen) { inclusive = true }
                    }
                }
            )
        }

        composable<NavRoute.SignInScreen> {
            SignInScreen(
                onSignInSuccess = onAuthComplete,
                onNavigateToSignUp = {
                    navController.navigate(NavRoute.SignUpScreen) {
                        popUpTo(NavRoute.SignInScreen) { inclusive = true }
                    }
                },
                onNavigateToForgotPassword = {
                    navController.navigate(NavRoute.ForgotPasswordScreen)
                }
            )
        }

        composable<NavRoute.ForgotPasswordScreen> {
            ForgotPasswordScreen(
                onResetSuccess = {
                    navController.navigate(NavRoute.SignInScreen) {
                        popUpTo(NavRoute.ForgotPasswordScreen) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.popBackStack()
                }
            )
        }
    }
}
