package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import kotlinx.coroutines.delay

private const val MAX_STAGGER_ITEMS = 12
private const val DURATION_DIVISOR = 2
private const val OFFSET_DIVISOR = 2

/**
 * Staggered slide-up + fade-in entry animation wrapper for grid items.
 *
 * @param index         position in the list — drives the stagger delay
 * @param delayPerItem  ms to wait between each item starting its animation (default 50ms)
 * @param initialOffset how far below the final position the item starts (default 32.dp)
 * @param durationMs    fade-in duration in milliseconds (default 300ms)
 */
@Composable
fun AnimatedEntry(
    modifier: Modifier = Modifier,
    index: Int = 0,
    delayPerItem: Int = Motion.STAGGER_DELAY,
    initialOffset: Dp = 32.dp,
    durationMs: Int = Motion.DURATION_STANDARD,
    skipStagger: Boolean = false,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val initialOffsetPx = with(density) { initialOffset.toPx() }

    // Use rememberSaveable to ensure the "visible" state persists even if the item
    // is temporarily removed from the composition (common in LazyColumn).
    var visible by androidx.compose.runtime.saveable.rememberSaveable { mutableStateOf(skipStagger) }

    // Lock in the skipStagger value when this item is first composed
    val shouldSkipStagger = remember { skipStagger }

    LaunchedEffect(Unit) {
        if (visible) return@LaunchedEffect // Already visible, skip animation logic

        if (shouldSkipStagger) {
            visible = true
        } else {
            // Use index % limit to avoid massive delays on large scrolling lists
            val staggerIndex = index % MAX_STAGGER_ITEMS
            delay((staggerIndex * delayPerItem).toLong())
            visible = true
        }
    }

    val finalDuration = if (shouldSkipStagger) durationMs / DURATION_DIVISOR else durationMs
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = finalDuration, easing = Motion.StandardEasing),
        label = "animated_entry_alpha_$index"
    )
    val translateY by animateFloatAsState(
        targetValue = if (visible) {
            0f
        } else if (shouldSkipStagger) {
            initialOffsetPx / OFFSET_DIVISOR
        } else {
            initialOffsetPx
        },
        animationSpec = spring(
            dampingRatio = if (shouldSkipStagger) Spring.DampingRatioNoBouncy else Spring.DampingRatioLowBouncy,
            stiffness = if (shouldSkipStagger) Spring.StiffnessMedium else Spring.StiffnessLow
        ),
        label = "animated_entry_y_$index"
    )

    Box(
        modifier = modifier.graphicsLayer {
            this.alpha = alpha
            this.translationY = translateY
        }
    ) {
        content()
    }
}
