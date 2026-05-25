package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients

interface BaseThemePalette {
    val primary: Color
    val onPrimary: Color
    val secondary: Color
    val onSecondary: Color
    val isDark: Boolean

    // --- Backgrounds & Surfaces ---
    val background: Color
    val onBackground: Color
    val surface: Color
    val onSurface: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val outline: Color

    // --- Text Colors ---
    val textPrimary: Color
    val textSecondary: Color
    val textTertiary: Color

    // --- Feedback ---
    val success: Color
    val warning: Color
    val error: Color
    val onError: Color

    // --- System UI ---
    val statusBarColor: Color
    val navBarColor: Color

    // --- Complex Tokens ---
    val gradients: AppGradients
}
