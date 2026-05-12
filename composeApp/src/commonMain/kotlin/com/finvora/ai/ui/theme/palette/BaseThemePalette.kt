package com.finvora.ai.ui.theme.palette

import androidx.compose.ui.graphics.Color
import com.finvora.ai.ui.theme.tokens.AppGradients
import com.finvora.ai.ui.theme.tokens.BottomSheetColors
import com.finvora.ai.ui.theme.tokens.FeatureIconGradients

interface BaseThemePalette {
    val primary: Color
    val onPrimary: Color

    val secondary: Color
    val onSecondary: Color

    val background: Color
    val onBackground: Color

    val surface: Color
    val onSurface: Color

    val error: Color
    val onError: Color

    // Extended Material 3 Colors
    val primaryContainer: Color
    val onPrimaryContainer: Color
    val secondaryContainer: Color
    val onSecondaryContainer: Color
    val tertiary: Color
    val onTertiary: Color
    val tertiaryContainer: Color
    val onTertiaryContainer: Color
    val surfaceVariant: Color
    val onSurfaceVariant: Color
    val outline: Color

    val isDark: Boolean

    val statusBarColor: Color
    val navBarColor: Color

    val gradients: AppGradients

    // Bottom Sheet Colors
    val bottomSheetColors: BottomSheetColors

    // Feature Icon Gradients
    val featureIconGradients: FeatureIconGradients

    // Text & Semantic Colors
    val textPrimary: Color
    val textSecondary: Color
    val textTertiary: Color
    val accent: Color
    val surfaceMedium: Color
    val surfaceDark: Color

    // Decorative Colors (Onboarding)
    val decorativeBlue: Color
    val decorativePurple: Color
    val decorativePink: Color
    val decorativeGreen: Color
    val decorativeYellow: Color

    // Onboarding Gradients
    val onboardingNextGradient: List<Color>
    val onboardingExploreGradient: List<Color>

    // Other UI colors
    val starYellow: Color
    val linkBlue: Color
}
