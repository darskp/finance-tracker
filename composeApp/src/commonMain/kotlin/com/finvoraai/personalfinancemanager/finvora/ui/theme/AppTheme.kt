package com.finvoraai.personalfinancemanager.finvora.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.font.FontWeight
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.BaseThemePalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.Dark
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.Light
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.Ocean
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.AppGradients

// Define CompositionLocals
val LocalIsDarkTheme = staticCompositionLocalOf { true }
val LocalAppPalette = staticCompositionLocalOf<BaseThemePalette> { Dark }
val LocalAppGradients = staticCompositionLocalOf<AppGradients> { Dark.gradients }

fun getAppPalette(theme: String?, systemIsDark: Boolean): BaseThemePalette {
    return when (theme?.lowercase()) {
        "light" -> Light
        "dark" -> Dark
        "ocean" -> Ocean
        "system" -> if (systemIsDark) Dark else Light
        else -> Light
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTheme(theme: String? = null, content: @Composable () -> Unit) {
    val systemIsDark = isSystemInDarkTheme()
    val palette = getAppPalette(theme, systemIsDark)

    val typography = Typography(
        displayLarge = H1TextStyle(),
        displayMedium = H2TextStyle(),
        displaySmall = H3TextStyle(),
        headlineLarge = H4TextStyle(),
        headlineMedium = H5TextStyle(),
        headlineSmall = H6TextStyle(),
        titleLarge = BodyXXLarge().copy(fontWeight = FontWeight.Bold),
        titleMedium = BodyXLarge().copy(fontWeight = FontWeight.Bold),
        titleSmall = BodyLarge().copy(fontWeight = FontWeight.Bold),
        bodyLarge = BodyLarge(),
        bodyMedium = BodyNormal(),
        bodySmall = BodySmall(),
        labelLarge = BodyNormal().copy(fontWeight = FontWeight.Bold),
        labelMedium = BodySmall().copy(fontWeight = FontWeight.Bold),
        labelSmall = BodyXSmall().copy(fontWeight = FontWeight.Bold)
    )

    // Choose the color scheme based on the palette properties
    // Choose the color scheme based on the palette properties
    val colorScheme = if (palette.isDark) {
        darkColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.onSurfaceVariant,
            error = palette.error,
            onError = palette.onError,
            outline = palette.outline
        )
    } else {
        lightColorScheme(
            primary = palette.primary,
            onPrimary = palette.onPrimary,
            secondary = palette.secondary,
            onSecondary = palette.onSecondary,
            background = palette.background,
            onBackground = palette.onBackground,
            surface = palette.surface,
            onSurface = palette.onSurface,
            surfaceVariant = palette.surfaceVariant,
            onSurfaceVariant = palette.onSurfaceVariant,
            error = palette.error,
            onError = palette.onError,
            outline = palette.outline
        )
    }

    CompositionLocalProvider(
        LocalIsDarkTheme provides palette.isDark,
        LocalAppPalette provides palette,
        LocalAppGradients provides palette.gradients,
        // Disable default overscroll glow (Android only)
        LocalOverscrollConfiguration provides null
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
