package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.StaticColors

object Light : BaseThemePalette {

    // --- Brand Design Tokens ---
    private val BrandPrimary = Color(0xFFD4A017) // Luxury Gold
    private val BrandSecondary = Color(0xFFF3EEE3)
    private val BrandBackground = Color(0xFFFFFBF5)
    private val BrandSurface = Color(0xFFFFFFFF)
    private val BrandOutline = Color(0xFFE0D4BC)

    private val TextPrimary = Color(0xFF111111)
    private val TextSecondary = Color(0xFF4B4B4B)
    private val OnBrandPrimary = Color(0xFF111111)
    
    // --- Feedback ---
    override val success = Color(0xFF2E7D32) // Emerald Green
    override val warning = Color(0xFFEF6C00) // Deep Orange
    override val error = Color(0xFFB3261E)
    override val onError = StaticColors.White

    // --- Core Palette ---
    override val primary = BrandPrimary
    override val onPrimary = OnBrandPrimary
    override val secondary = BrandSecondary
    override val onSecondary = TextPrimary
    override val isDark = false

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
                Color(0xFFFFFBF5),
                Color(0xFFFAF3E5),
                Color(0xFFFFFBF5)
            )
        ),
        titleGradient = Brush.linearGradient(
            colors = listOf(TextPrimary, BrandPrimary)
        ),
        onboardingNextGradient = listOf(BrandPrimary, Color(0xFFFFD166)),
        onboardingExploreGradient = listOf(BrandPrimary, Color(0xFFE6B93C), Color(0xFFB8860B))
    )
}
