package com.finvoraai.personalfinancemanager.finvora.ui.skeleton

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

/**
 * Internal constants for the Glass Skeleton design system.
 * All values are opinionated — callers should never need to override these.
 */
internal object SkeletonDefaults {
    /** Total sweep duration in milliseconds. 1400ms feels natural, not mechanical. */
    const val DURATION_MS = 1400

    /** Base layer alpha — semi-transparent surface to let background show through. */
    const val BASE_ALPHA = 0.30f

    /** Mid-layer alpha — the ramp up/down on either side of the shine. */
    const val MID_ALPHA = 0.50f

    /**
     * Shine alpha — how bright the glass reflection appears.
     * Uses onSurface color so it adapts naturally to light and dark themes.
     */
    const val SHINE_ALPHA = 0.13f

    /**
     * Width of the moving glass reflection in dp.
     * A broad shine (100dp) looks like light on glass, not a laser beam.
     */
    val SHINE_WIDTH_DP = 100.dp

    /** Default corner radius — sits between cards (16dp) and chips (8dp). */
    val DEFAULT_SHAPE = RoundedCornerShape(12.dp)
}
