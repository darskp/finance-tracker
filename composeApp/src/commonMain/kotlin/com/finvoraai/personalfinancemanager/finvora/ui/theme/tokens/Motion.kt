package com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.runtime.Immutable

@Immutable
object Motion {
    // Durations
    const val DURATION_MICRO = 200
    const val DURATION_FADE = 220
    const val DURATION_SNAP = 250
    const val DURATION_STANDARD = 300
    const val DURATION_EMPHASIS = 450

    // Easings
    val StandardEasing: Easing = FastOutSlowInEasing
    val DecelerateEasing: Easing = LinearOutSlowInEasing

    // Stagger Delays
    const val STAGGER_DELAY = 50

    // Shared Timing and Animation Tokens
    const val TYPING_CHAR_DELAY = 80L
    const val SPLASH_EXIT_DELAY = 1000L
    const val OTP_TIMER_SECONDS = 60
    const val ONE_SECOND_MILLIS = 1000L
    const val REFRESH_DELAY_MILLIS = 1000L
    const val APP_BAR_ANIMATION_DURATION = 500
}
