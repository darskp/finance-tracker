package com.finvora.ai.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvora.ai.ui.theme.tokens.AppGradients
import com.finvora.ai.ui.theme.tokens.BottomSheetColors
import com.finvora.ai.ui.theme.tokens.FeatureIconGradients

object Dark : BaseThemePalette {
    override val primary = Color(0xFFD0BCFF)
    override val onPrimary = Color(0xFF381E72)
    override val secondary = Color(0xFFCCC2DC)
    override val onSecondary = Color(0xFF332D41)
    override val background = Color(0xFF0B0B0D)
    override val onBackground = Color(0xFFE6E1E5)
    override val surface = Color(0xFF0B0B0D)
    override val onSurface = Color(0xFFE6E1E5)
    override val error = Color(0xFFF2B8B5)
    override val onError = Color(0xFF601410)

    // Extended Material 3 Colors
    override val primaryContainer = Color(0xFF4F378B)
    override val onPrimaryContainer = Color(0xFFEADDFF)
    override val secondaryContainer = Color(0xFF4A4458)
    override val onSecondaryContainer = Color(0xFFE8DEF8)
    override val tertiary = Color(0xFFEFB8C8)
    override val onTertiary = Color(0xFF492532)
    override val tertiaryContainer = Color(0xFF633B48)
    override val onTertiaryContainer = Color(0xFFFFD8E4)
    override val surfaceVariant = Color(0xFF1A1A1C)
    override val onSurfaceVariant = Color(0xFFCAC4D0)
    override val outline = Color(0xFF938F99)
    override val isDark = true
    override val statusBarColor = Color.Transparent
    override val navBarColor = Color.Transparent
    
    // gradients
    override val gradients = AppGradients(
        screenBackground = Brush.linearGradient(
            colors = listOf(
                Color(0xFF0B0B0D),
                Color(0xFF0B0B0D)
            )
        ),
        titleGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE63946), // Red
                Color(0xFFF1FA8C), // Pale Yellow
                Color(0xFF5D5D5D)  // Lightened Gray for visibility
            )
        )
    )

    // Bottom Sheet Colors
    override val bottomSheetColors = BottomSheetColors(
        background = Color(0xFF0B0B0D),
        itemBackground = Color(0xFF121214),
        accent = Color(0xFF9B59B6),
        textPrimary = Color.White,
        textSecondary = Color.White.copy(alpha = 0.45f),
        dragHandle = Color.White.copy(alpha = 0.25f),
        arrow = Color.White.copy(alpha = 0.3f),
        ripple = Color.White.copy(alpha = 0.08f)
    )

    // Feature Icon Gradients
    override val featureIconGradients = FeatureIconGradients(
        explore = listOf(Color(0xFF667EEA), Color(0xFF764BA2)),
        favorites = listOf(Color(0xFFFF6B6B), Color(0xFFEE5A24)),
        search = listOf(Color(0xFF11998E), Color(0xFF38EF7D)),
        categories = listOf(Color(0xFFF2994A), Color(0xFFF2C94C)),
        settings = listOf(Color(0xFF8E9AAF), Color(0xFFCBD4E6)),
        aboutUs = listOf(Color(0xFF9B59B6), Color(0xFF5F33E1)),
        shareApp = listOf(Color(0xFF00B4DB), Color(0xFF0083B0)),
        rateApp = listOf(Color(0xFFFFD700), Color(0xFFFFA500))
    )

    override val textPrimary = Color(0xFFE0E0E0)
    override val textSecondary = Color(0xFF9E9E9E)
    override val textTertiary = Color(0xFF616161)
    override val accent = Color(0xFF7E57C2)
    override val surfaceMedium = Color(0xFF121214)
    override val surfaceDark = Color(0xFF0B0B0D)

    override val decorativeBlue = Color(0xFF87CEEB)
    override val decorativePurple = Color(0xFFB19CD9)
    override val decorativePink = Color(0xFFFFB6C1)
    override val decorativeGreen = Color(0xFF90EE90)
    override val decorativeYellow = Color(0xFFFFE66D)

    override val onboardingNextGradient = listOf(Color(0xFF7B5CF0), Color(0xFF5F33E1))
    override val onboardingExploreGradient = listOf(Color(0xFF9B59B6), Color(0xFF5F33E1), Color(0xFF3B1FA8))

    override val starYellow = Color(0xFFFFC107)
    override val linkBlue = Color(0xFF64B5F6)
}
