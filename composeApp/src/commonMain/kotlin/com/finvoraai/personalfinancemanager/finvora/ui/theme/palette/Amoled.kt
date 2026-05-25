package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.StaticColors

object Amoled : BaseThemePalette {

    // --- Brand Design Tokens ---
    private val BrandPrimary = Color(0xFF4FDEA3)
    private val BrandSecondary = Color(0xFF0B0B0B)
    private val BrandBackground = Color(0xFF000000)
    private val BrandSurface = Color(0xFF101010)
    private val BrandOutline = Color(0xFF242424)

    private val TextPrimary = Color(0xFFF5F5F5)
    private val TextSecondary = Color(0xFFB8B8B8)
    private val OnBrandPrimary = Color(0xFF002B1C)

    // --- Feedback ---
    override val success = Color(0xFF81C784)
    override val warning = Color(0xFFFFB74D)
    override val error = Color(0xFFF2B8B5)
    override val onError = Color(0xFF601410)

    // --- Core Palette ---
    override val primary = BrandPrimary
    override val onPrimary = OnBrandPrimary
    override val secondary = BrandSecondary
    override val onSecondary = TextPrimary
    override val isDark = true

    // --- Backgrounds & Surfaces ---
    override val background = BrandBackground
    override val onBackground = TextPrimary
    override val surface = BrandSurface
    override val onSurface = TextPrimary
    override val surfaceVariant = BrandSecondary
    override val onSurfaceVariant = TextSecondary
    override val outline = BrandOutline

    // --- Text Hierarchy ---
    override val textPrimary = TextPrimary
    override val textSecondary = TextSecondary
    override val textTertiary = TextSecondary.copy(alpha = 0.7f)

    // --- System UI ---
    override val statusBarColor = StaticColors.Transparent
    override val navBarColor = StaticColors.Transparent

    // --- Complex Tokens ---
    override val gradients = AppGradients(
        screenBackground = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF000000),
                Color(0xFF050505),
                Color(0xFF000000)
            )
        ),

        titleGradient = Brush.linearGradient(
            colors = listOf(
                TextPrimary,
                BrandPrimary
            )
        ),

        onboardingNextGradient = listOf(
            BrandPrimary,
            Color(0xFF34D399)
        ),

        onboardingExploreGradient = listOf(
            BrandPrimary,
            Color(0xFF10B981),
            Color(0xFF059669)
        )
    )
}