package com.finvora.ai.ui.theme.tokens

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.runtime.Immutable

@Immutable
object Motion {
    // Durations
    const val DurationMicro = 200
    const val DurationFade = 220
    const val DurationSnap = 250
    const val DurationStandard = 300
    const val DurationEmphasis = 450

    // Easings
    val StandardEasing: Easing = FastOutSlowInEasing
    val DecelerateEasing: Easing = LinearOutSlowInEasing

    // Stagger Delays
    const val StaggerDelay = 50
}
