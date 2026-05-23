package com.finvoraai.personalfinancemanager

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.finvoraai.personalfinancemanager.finvora.feature.main.MainScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.AppTheme
import org.koin.compose.KoinContext

@Composable
fun App(isColdStart: Boolean = true) {
    KoinContext {
        AppTheme {
            val navController = rememberNavController()
            MainScreen(navController, isColdStart)
        }
    }
}
