package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.BottomSheetColors

object Dark : BaseThemePalette {
    // --- Brand Design Tokens ---
    private val BrandPrimary = Color(0xFF4FDEA3)
    private val BrandSecondary = Color(0xFF242C27)
    private val BrandBackground = Color(0xFF0E1511)
    private val BrandSurface = Color(0xFF161D19)
    private val BrandOutline = Color(0xFF3C4A42)

    private val TextTitle = Color(0xFFDDE4DD)
    private val TextBody = Color(0xFFBBCABF)
    private val TextBadge = Color(0xFFABBDB0) // VERSION text color
    private val OnBrandPrimary = Color(0xFF003824)
    private val BadgeBackground = Color(0xFF3D4D43).copy(alpha = 0.3f) // 4c alpha
    private val White = Color(0xFFFFFFFF)

    // --- Core Palette ---
    override val primary = BrandPrimary
    override val onPrimary = OnBrandPrimary
    override val secondary = BrandSecondary
    override val onSecondary = TextTitle
    override val accent = BrandPrimary
    override val isDark = true

    // --- Backgrounds & Surfaces ---
    override val background = BrandBackground
    override val onBackground = TextTitle
    override val surface = BrandSurface
    override val onSurface = TextTitle
    override val surfaceVariant = BrandSecondary
    override val onSurfaceVariant = TextBody
    override val surfaceMedium = BrandSecondary
    override val surfaceDark = BrandBackground
    override val outline = BrandOutline

    // --- Text Hierarchy ---
    override val textPrimary = TextTitle
    override val textSecondary = TextBody
    override val textTertiary = White.copy(alpha = 0.7f)

    // --- Feedback ---
    override val error = Color(0xFFF2B8B5)
    override val onError = Color(0xFF601410)
    override val starYellow = Color(0xFFFFC107)
    override val linkBlue = Color(0xFF64B5F6)

    // --- Material 3 Containers ---
    override val primaryContainer = BadgeBackground
    override val onPrimaryContainer = TextBadge
    override val secondaryContainer = BrandSecondary
    override val onSecondaryContainer = TextTitle
    override val tertiary = BrandPrimary
    override val onTertiary = OnBrandPrimary
    override val tertiaryContainer = BrandSecondary
    override val onTertiaryContainer = White

    // --- System UI ---
    override val statusBarColor = Color.Transparent
    override val navBarColor = Color.Transparent

    // --- Complex Tokens ---
    override val gradients = AppGradients(
        screenBackground = Brush.linearGradient(
            colors = listOf(BrandBackground, BrandBackground)
        ),
        titleGradient = Brush.linearGradient(
            colors = listOf(TextTitle, BrandPrimary)
        )
    )

    override val bottomSheetColors = BottomSheetColors(
        background = BrandBackground,
        itemBackground = BrandSecondary,
        accent = BrandPrimary,
        textPrimary = TextTitle,
        textSecondary = TextBody,
        dragHandle = White.copy(alpha = 0.25f),
        arrow = TextTitle.copy(alpha = 0.3f),
        ripple = BrandPrimary.copy(alpha = 0.08f)
    )

    // --- Decorative (Signature Blurs & Onboarding) ---
    override val decorativeBlue = BrandPrimary
    override val decorativePurple = Color(0xFF8B5CF6)
    override val decorativePink = Color(0xFFEC4899)
    override val decorativeGreen = BrandPrimary
    override val decorativeYellow = Color(0xFFFBBF24)

    override val onboardingNextGradient = listOf(BrandPrimary, Color(0xFF34D399))
    override val onboardingExploreGradient = listOf(BrandPrimary, Color(0xFF10B981), Color(0xFF059669))
}
