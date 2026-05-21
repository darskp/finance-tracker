package com.finvoraai.personalfinancemanager.finvora.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavRoute {

    @Serializable
    data object SplashScreen : NavRoute()

    @Serializable
    data object Onboarding : NavRoute()

    @Serializable
    data object WelcomeScreen : NavRoute()

    @Serializable
    data object HomeScreen : NavRoute()

    @Serializable
    data object SignInScreen : NavRoute()

    @Serializable
    data object SignUpScreen : NavRoute()

    @Serializable
    data object ForgotPasswordScreen : NavRoute()
}

/**
 * Root level navigation graph routes
 */
@Serializable
sealed class RootNavGraph(val route: String) {
    @Serializable
    data object Onboarding : RootNavGraph("onboarding_graph")

    @Serializable
    data object Main : RootNavGraph("main_graph")
}
