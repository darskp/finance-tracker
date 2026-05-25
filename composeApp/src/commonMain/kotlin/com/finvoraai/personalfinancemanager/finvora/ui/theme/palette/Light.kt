package com.finvoraai.personalfinancemanager.finvora.ui.theme.palette

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.BottomSheetColors

object Light : BaseThemePalette {

    // --- Brand Design Tokens ---
    private val BrandPrimary = Color(0xFFD4A017) // Luxury Gold
    private val BrandSecondary = Color(0xFFF3EEE3)
    private val BrandBackground = Color(0xFFFFFBF5)
    private val BrandSurface = Color(0xFFFFFFFF)
    private val BrandOutline = Color(0xFFE0D4BC)

    private val TextTitle = Color(0xFF111111) // Rich Black
    private val TextBody = Color(0xFF4B4B4B)
    private val TextBadge = Color(0xFF6B6255)

    private val OnBrandPrimary = Color(0xFF111111)
    private val BadgeBackground = Color(0xFFFFF3D6)
    private val White = Color(0xFFFFFFFF)

    // --- Core Palette ---
    override val primary = BrandPrimary
    override val onPrimary = OnBrandPrimary
    override val secondary = BrandSecondary
    override val onSecondary = TextTitle
    override val accent = BrandPrimary
    override val isDark = false

    // --- Backgrounds & Surfaces ---
    override val background = BrandBackground
    override val onBackground = TextTitle
    override val surface = BrandSurface
    override val onSurface = TextTitle
    override val surfaceVariant = BrandSecondary
    override val onSurfaceVariant = TextBody
    override val surfaceMedium = Color(0xFFFAF4E8)
    override val surfaceDark = Color(0xFFF0E6D2)
    override val outline = BrandOutline

    // --- Text Hierarchy ---
    override val textPrimary = TextTitle
    override val textSecondary = TextBody
    override val textTertiary = TextBody.copy(alpha = 0.7f)

    // --- Feedback ---
    override val error = Color(0xFFB3261E)
    override val onError = White
    override val starYellow = Color(0xFFFFC107)
    override val linkBlue = Color(0xFF1D4ED8)

    // --- Material 3 Containers ---
    override val primaryContainer = BadgeBackground
    override val onPrimaryContainer = TextBadge
    override val secondaryContainer = BrandSecondary
    override val onSecondaryContainer = TextTitle
    override val tertiary = BrandPrimary
    override val onTertiary = TextTitle
    override val tertiaryContainer = Color(0xFFFFF6E4)
    override val onTertiaryContainer = TextTitle

    // --- System UI ---
    override val statusBarColor = Color.Transparent
    override val navBarColor = Color.Transparent

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
            colors = listOf(
                TextTitle,
                BrandPrimary
            )
        )
    )

    override val bottomSheetColors = BottomSheetColors(
        background = BrandSurface,
        itemBackground = BrandSecondary,
        accent = BrandPrimary,
        textPrimary = TextTitle,
        textSecondary = TextBody,
        dragHandle = Color.Black.copy(alpha = 0.15f),
        arrow = TextTitle.copy(alpha = 0.3f),
        ripple = BrandPrimary.copy(alpha = 0.08f)
    )

    // --- Decorative (Signature Blurs & Onboarding) ---
    override val decorativeBlue = Color(0xFF2563EB)
    override val decorativePurple = Color(0xFF7C3AED)
    override val decorativePink = Color(0xFFD4A017)
    override val decorativeGreen = Color(0xFF059669)
    override val decorativeYellow = BrandPrimary

    override val onboardingNextGradient = listOf(
        BrandPrimary,
        Color(0xFFFFD166)
    )

    override val onboardingExploreGradient = listOf(
        BrandPrimary,
        Color(0xFFE6B93C),
        Color(0xFFB8860B)
    )
}
