package com.finvoraai.personalfinancemanager.finvora.ui.skeleton

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State

/**
 * Internal animation engine for the Glass Skeleton system.
 *
 * Returns a [State<Float>] in the range [0f, 1f] representing how far the
 * glass reflection has swept across the placeholder (0 = left edge, 1 = right edge).
 *
 * The caller is responsible for mapping this progress to pixel coordinates.
 */
@Composable
internal fun rememberSkeletonProgress(): State<Float> {
    val transition = rememberInfiniteTransition(label = "glass_skeleton")
    return transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = SkeletonDefaults.DURATION_MS,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "skeleton_progress"
    )
}
