package com.finvoraai.personalfinancemanager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.finvoraai.personalfinancemanager.finvora.data.local.SettingsDao
import com.finvoraai.personalfinancemanager.finvora.feature.main.MainScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.AppTheme
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App(isColdStart: Boolean = true) {
    KoinContext {
        val settingsDao: SettingsDao = koinInject()
        val themeSetting by settingsDao.getThemeSetting().collectAsState(initial = null)
        val currentTheme = themeSetting?.theme ?: "System"

        AppTheme(theme = currentTheme) {
            val navController = rememberNavController()
            MainScreen(navController, isColdStart)
        }
    }
}
