package com.finvoraai.personalfinancemanager.finvora.core.debug

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.finvoraai_logo_no_text
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

private val BUTTON_SIZE = 38.dp
private val LOGO_SIZE = 24.dp
private val BORDER_WIDTH = 1.5.dp

/**
 * Always-visible Finvora logo button — top-right corner of the screen.
 *
 * Tap 3 times within 1500ms to open/close the debug overlay.
 * Glows green when overlay is open, dark when hidden.
 *
 */
@Composable
fun DebugFloatingButton(modifier: Modifier = Modifier) {
    if (!DebugController.isEnabled()) return

    val isOverlayVisible by DebugController.isVisible.collectAsState()

    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
            .size(BUTTON_SIZE)
            .clip(CircleShape)
            .background(
                if (isOverlayVisible) {
                    DebugColors.ButtonActiveGreen.copy(alpha = DebugColors.BUTTON_ACTIVE_ALPHA)
                } else {
                    DebugColors.ButtonInactiveBg.copy(alpha = DebugColors.BUTTON_INACTIVE_ALPHA)
                }
            )
            .border(
                width = BORDER_WIDTH,
                color = if (isOverlayVisible) DebugColors.ButtonActiveGreen else DebugColors.ButtonInactiveBorder,
                shape = CircleShape
            )
            .clickable { DebugController.onLogoTapped() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.finvoraai_logo_no_text),
            contentDescription = "Debug Trigger — tap 3 times",
            modifier = Modifier.size(LOGO_SIZE),
            contentScale = ContentScale.Fit
        )
    }
}
