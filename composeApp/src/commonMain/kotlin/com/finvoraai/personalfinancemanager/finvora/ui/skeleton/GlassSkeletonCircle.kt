package com.finvoraai.personalfinancemanager.finvora.ui.skeleton

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

/**
 * A circular glass-reflection placeholder. Convenience wrapper around [GlassSkeleton].
 *
 * # Usage
 * ```kotlin
 * // Avatar / profile icon placeholder
 * GlassSkeletonCircle(size = 48.dp)
 *
 * // Small icon button placeholder
 * GlassSkeletonCircle(size = 32.dp)
 * ```
 *
 * @param size Diameter of the circle.
 */
@Composable
fun GlassSkeletonCircle(
    size: Dp,
    modifier: Modifier = Modifier
) {
    GlassSkeleton(
        modifier = modifier.size(size),
        shape = CircleShape
    )
}
