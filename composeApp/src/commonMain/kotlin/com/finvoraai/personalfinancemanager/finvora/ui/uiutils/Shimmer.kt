package com.finvoraai.personalfinancemanager.finvora.ui.uiutils

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

/**
 * A modern, high-performance shimmer modifier for loading states.
 */
fun Modifier.shimmer(
    visible: Boolean = true,
    baseColor: Color = Color.Gray.copy(alpha = 0.1f),
    highlightColor: Color = Color.Gray.copy(alpha = 0.3f),
    durationMillis: Int = 1200
): Modifier = composed {
    if (!visible) return@composed this

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (size.width > 0) 2f * size.width.toFloat() else 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(translateAnim - size.width.toFloat(), 0f),
        end = Offset(translateAnim, size.height.toFloat())
    )

    this.background(brush)
        .onGloballyPositioned {
            size = it.size
        }
}
