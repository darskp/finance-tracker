package com.finvoraai.personalfinancemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.Dark

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val isColdStart = savedInstanceState == null

        splashScreen.setKeepOnScreenCondition { false }

        initKoin()

        setContent {
            val palette = Dark

            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(palette.statusBarColor.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(palette.navBarColor.toArgb())
            )

            Surface(
                color = palette.background
            ) {
                App(isColdStart = isColdStart)
            }
        }
    }
}
