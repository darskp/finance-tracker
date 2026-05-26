package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.palette.BaseThemePalette

@Composable
fun AppBackgroundScreen(
    modifier: Modifier = Modifier,
    shouldShowDotsAndIcons: Boolean = false,
    showTopCenterBlur: Boolean = false,
    content: @Composable BoxWithConstraintsScope.() -> Unit = {}
) {
    val palette = LocalAppPalette.current

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(palette.gradients.screenBackground)
    ) {
        DecorativeBlur(
            color = palette.primary,
            size = Spacing.s125,
            blurRadius = Spacing.s15,
            alpha = 0.05f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = Spacing.s25, y = -Spacing.s25)
        )

        DecorativeBlur(
            color = palette.primary,
            size = Spacing.s100,
            blurRadius = Spacing.s12Half,
            alpha = 0.05f,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = -Spacing.s30, y = Spacing.s25)
        )

        if (showTopCenterBlur) {
            DecorativeBlur(
                color = palette.primary,
                size = TOP_CENTER_BLUR_SIZE,
                blurRadius = Spacing.s20,
                alpha = TOP_CENTER_BLUR_ALPHA,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = TOP_CENTER_BLUR_OFFSET)
            )
        }

        if (shouldShowDotsAndIcons) {
            FloatingElements(palette = palette)
        }

        content()
    }
}

@Composable
private fun DecorativeBlur(color: Color, size: Dp, blurRadius: Dp, alpha: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(size)
            .blur(
                radius = blurRadius,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .background(
                color = color.copy(alpha = alpha),
                shape = CircleShape
            )
    )
}

@Composable
private fun BoxWithConstraintsScope.FloatingElements(palette: BaseThemePalette) {
    ColoredDot(color = palette.primary.copy(alpha = 0.1f), x = Spacing.s12, y = Spacing.s40, size = Spacing.s2)
    ColoredDot(color = palette.primary.copy(alpha = 0.05f), x = Spacing.s55, y = Spacing.s100, size = Spacing.s3)
}

@Composable
fun ColoredDot(color: Color, x: Dp, y: Dp, size: Dp) {
    Box(
        modifier = Modifier
            .offset(x = x, y = y)
            .size(size)
            .background(color, shape = CircleShape)
    )
}

private val TOP_CENTER_BLUR_SIZE = 360.dp
private val TOP_CENTER_BLUR_OFFSET = (-180).dp
private const val TOP_CENTER_BLUR_ALPHA = 0.12f
