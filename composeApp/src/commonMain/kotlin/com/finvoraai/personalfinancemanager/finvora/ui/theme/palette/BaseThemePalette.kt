package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.BottomSheetColors

interface BaseThemePalette {
    val primary: Color
    val onPrimary: Color
    val secondary: Color
    val onSecondary: Color
    val accent: Color
    val isDark: Boolean

    val background: Color
    val onBackground: Color
    val surface: Color
    val onSurface: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val surfaceMedium: Color
    val surfaceDark: Color
    val outline: Color

    // --- Text Colors ---
    val textPrimary: Color
    val textSecondary: Color
    val textTertiary: Color

    val error: Color
    val onError: Color
    val starYellow: Color
    val linkBlue: Color

    val primaryContainer: Color
    val onPrimaryContainer: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color
    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color

    val statusBarColor: Color
    val navBarColor: Color

    val gradients: AppGradients
    val bottomSheetColors: BottomSheetColors

    val decorativeBlue: Color
    val decorativePurple: Color
    val decorativePink: Color
    val decorativeGreen: Color
    val decorativeYellow: Color
    val onboardingNextGradient: List<Color>
    val onboardingExploreGradient: List<Color>
}
