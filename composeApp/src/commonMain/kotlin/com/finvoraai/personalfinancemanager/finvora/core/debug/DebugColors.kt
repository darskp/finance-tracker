@file:Suppress("ForbiddenImport")

package com.finvoraai.personalfinancemanager.finvora.core.debug

import androidx.compose.ui.graphics.Color

/** Shared design tokens for the debug overlay system. */
@Suppress("MagicNumber")
internal object DebugColors {
    val Auth = Color(0xFF64FFDA)
    val Onboarding = Color(0xFFFFD740)
    val Navigation = Color(0xFFFF6E40)
    val Api = Color(0xFF82B1FF)
    val Database = Color(0xFFEF9A9A)
    val Generic = Color(0xFFBB86FC)
    val TextMuted = Color(0xFF8B949E)

    // Floating button
    val ButtonActiveGreen = Color(0xFF00E676)
    val ButtonInactiveBg = Color(0xFF1A1F2E)
    val ButtonInactiveBorder = Color(0xFF2E3250)

    const val BUTTON_ACTIVE_ALPHA = 0.92f
    const val BUTTON_INACTIVE_ALPHA = 0.88f
}
