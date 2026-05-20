package com.finvoraai.personalfinancemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.OnBoardingViewModel
import com.finvoraai.personalfinancemanager.finvora.ui.navigation.NavRoute
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.Dark
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val onBoardingViewModel: OnBoardingViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val isColdStart = savedInstanceState == null

        var startRoute: NavRoute? by mutableStateOf(null)

        // Hold the system splash screen until we know the route
        splashScreen.setKeepOnScreenCondition {
            startRoute == null
        }

        lifecycleScope.launch {
            startRoute = onBoardingViewModel.getCurrentRoute()
        }

        initKoin()

        setContent {
            val route = startRoute ?: return@setContent
            val palette = Dark

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(palette.statusBarColor.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(palette.navBarColor.toArgb())
            )

            Surface(
                color = palette.background
            ) {
                App(initialRoute = route, isColdStart = isColdStart)
            }
        }
    }
}
