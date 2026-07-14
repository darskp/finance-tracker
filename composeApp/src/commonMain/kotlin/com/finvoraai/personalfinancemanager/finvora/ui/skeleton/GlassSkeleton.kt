package com.finvoraai.personalfinancemanager.finvora.ui.skeleton

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette

/**
 * A universal glass-reflection placeholder used during loading states.
 *
 * # Usage
 * ```kotlin
 * // Card-sized block
 * GlassSkeleton(Modifier.fillMaxWidth().height(140.dp))
 *
 * // Text-line sized block
 * GlassSkeleton(Modifier.width(120.dp).height(16.dp), shape = RoundedCornerShape(4.dp))
 *
 * // Full-width transaction row
 * GlassSkeleton(Modifier.fillMaxWidth().height(72.dp), shape = RoundedCornerShape(12.dp))
 * ```
 *
 * # Design
 * - Base: `surfaceVariant` at 30% alpha — shows through the screen background.
 * - Shine: `onSurface` at 13% alpha — adapts to Light/Dark/Ocean themes automatically.
 * - Gradient width: 100dp — a wide reflection feels like real glass, not a laser.
 * - Duration: 1400ms — smooth and unhurried.
 *
 * @param modifier Controls size and position. Required — the caller defines the shape through size.
 * @param shape Corner radius. Defaults to 12dp. Match to the real component (card=16dp, chip=8dp, button=50%).
 */
@Composable
fun GlassSkeleton(
    modifier: Modifier = Modifier,
    shape: Shape = SkeletonDefaults.DEFAULT_SHAPE
) {
    val palette = LocalAppPalette.current
    val progress by rememberSkeletonProgress()

    // Read all theme colors once — stable references used inside drawWithCache
    val baseColor = palette.surfaceVariant
    val shineColor = palette.onSurface

    val skeletonModifier = Modifier
        .clip(shape)
        .drawWithCache {
            // The color list is computed here (cache layer) — only recreated when size changes.
            // The Brush offset position is computed in onDrawBehind — recreated per frame.
            val shineWidthPx = SkeletonDefaults.SHINE_WIDTH_DP.toPx()
            // Total travel: from -shineWidth (fully off left) to size.width (fully off right)
            val totalTravel = size.width + shineWidthPx
            val startX = (progress * totalTravel) - shineWidthPx

            val colors = listOf(
                baseColor.copy(alpha = SkeletonDefaults.BASE_ALPHA),
                baseColor.copy(alpha = SkeletonDefaults.MID_ALPHA),
                shineColor.copy(alpha = SkeletonDefaults.SHINE_ALPHA),
                baseColor.copy(alpha = SkeletonDefaults.MID_ALPHA),
                baseColor.copy(alpha = SkeletonDefaults.BASE_ALPHA)
            )

            onDrawBehind {
                // Diagonal sweep: top-left to bottom-right of the shine band
                val brush = Brush.linearGradient(
                    colors = colors,
                    start = Offset(startX, 0f),
                    end = Offset(startX + shineWidthPx, size.height)
                )
                drawRect(brush = brush)
            }
        }

    Box(modifier = modifier.then(skeletonModifier))
}
