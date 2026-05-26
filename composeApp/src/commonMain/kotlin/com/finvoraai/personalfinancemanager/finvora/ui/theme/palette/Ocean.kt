package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.StaticColors

object Ocean : BaseThemePalette {

    // --- Brand Design Tokens ---
    private val BrandPrimary = Color(0xFF2EA7FF)
    private val BrandSecondary = Color(0xFFEAF4FB)
    private val BrandBackground = Color(0xFFF7FBFF)
    private val BrandSurface = Color(0xFFFFFFFF)
    private val BrandOutline = Color(0xFFD2E3F0)

    private val TextPrimary = Color(0xFF0F172A)
    private val TextSecondary = Color(0xFF526072)
    private val OnBrandPrimary = StaticColors.White

    // --- Feedback ---
    override val success = Color(0xFF2E7D32)
    override val warning = Color(0xFFEF6C00)
    override val error = Color(0xFFC62828)
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
                Color(0xFFF7FBFF),
                Color(0xFFEEF7FF),
                Color(0xFFF7FBFF)
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
            Color(0xFF67C6FF)
        ),

        onboardingExploreGradient = listOf(
            BrandPrimary,
            Color(0xFF4AB6FF),
            Color(0xFF178BDB)
        )
    )
}
