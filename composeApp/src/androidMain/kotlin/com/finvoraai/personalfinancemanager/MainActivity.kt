package com.finvoraai.personalfinancemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.ui.theme.getAppPalette

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        val isColdStart = savedInstanceState == null

        splashScreen.setKeepOnScreenCondition { false }

        initKoin()

        setContent {
            val settingsDao = getKoin().get<SettingsDao>()
            val themeSetting by settingsDao.getThemeSetting().collectAsState(initial = null)
            val currentTheme = themeSetting?.theme ?: "System"
            val systemIsDark = isSystemInDarkTheme()
            val palette = getAppPalette(currentTheme, systemIsDark)

            val statusBarStyle = if (palette.isDark) {
                SystemBarStyle.dark(palette.statusBarColor.toArgb())
            } else {
                SystemBarStyle.light(palette.statusBarColor.toArgb(), palette.statusBarColor.toArgb())
            }

            val navigationBarStyle = if (palette.isDark) {
                SystemBarStyle.dark(palette.navBarColor.toArgb())
            } else {
                SystemBarStyle.light(palette.navBarColor.toArgb(), palette.navBarColor.toArgb())
            }

            enableEdgeToEdge(
                statusBarStyle = statusBarStyle,
                navigationBarStyle = navigationBarStyle
            )

            Surface(
                color = palette.background
            ) {
                App(isColdStart = isColdStart)
            }
        }
    }
}
