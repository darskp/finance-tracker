package com.finvoraai.personalfinancemanager.finvora.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class AppThemeType(val title: String) {
    DARK("Dark")
    ;

    companion object {
        @Suppress("UnusedParameter")
        fun from(name: String?): AppThemeType = DARK
    }
}

object Spacing {
    val default: Dp = 0.dp
    val hairline: Dp = 1.dp // Used for dividers and borders
    val sHalf: Dp = 2.dp
    val s1: Dp = 4.dp // 4dp
    val s1Half: Dp = 5.dp // 5dp
    val s2: Dp = 8.dp // 8dp
    val s2Half: Dp = 10.dp // 10dp
    val s3: Dp = 12.dp // 12dp
    val s3Half: Dp = 14.dp // 14dp
    val s4: Dp = 16.dp // 16dp
    val s5: Dp = 20.dp // 20dp
    val s6: Dp = 24.dp // 24dp
    val s7: Dp = 28.dp // 28dp
    val s8: Dp = 32.dp // 32dp
    val s9: Dp = 36.dp // 36dp
    val s10: Dp = 40.dp // 40dp
    val s11: Dp = 44.dp // 44dp
    val s12: Dp = 48.dp
    val s12Half: Dp = 50.dp
    val s13: Dp = 52.dp
    val s14: Dp = 56.dp // 56dp
    val s15: Dp = 60.dp // 60dp
    val s16: Dp = 64.dp // 64dp
    val s17: Dp = 68.dp // 68dp
    val s18: Dp = 72.dp // 72dp
    val s19: Dp = 76.dp // 76dp
    val s20: Dp = 80.dp // 80dp
    val s21: Dp = 84.dp // 84dp
    val s22: Dp = 88.dp // 88dp
    val s23: Dp = 92.dp // 92dp
    val s24: Dp = 96.dp // 96dp
    val s25: Dp = 100.dp
    val s30: Dp = 120.dp
    val s40: Dp = 160.dp
    val s55: Dp = 220.dp
    val s100: Dp = 400.dp
    val s125: Dp = 500.dp
    val s150: Dp = 600.dp

    // Layout constraint tokens
    val authMaxWidth: Dp = 480.dp
    const val AUTH_LOGO_WIDTH_FRACTION = 0.8f
    const val SETTINGS_CARD_HEIGHT_FRACTION = 0.15f
    const val GRADIENT_START_Y = 300f
    val borderMedium: Dp = 1.5.dp
    val iconMedium: Dp = 18.dp
}
