package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing

/**
 * A standard card container used throughout FinvoraAI.
 * Automatically aligns with the brand design language, using the current theme's
 * surface color, outline border, and standard rounded corners.
 */
@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    backgroundColor: Color? = null,
    border: BorderStroke? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val palette = LocalAppPalette.current
    val finalBgColor = backgroundColor ?: palette.surface
    val finalBorder = border ?: BorderStroke(Spacing.hairline, palette.outline)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.bounceClickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .clip(RoundedCornerShape(Spacing.s4))
            .background(finalBgColor)
            .border(
                border = finalBorder,
                shape = RoundedCornerShape(Spacing.s4)
            )
            .padding(Spacing.s4)
    ) {
        content()
    }
}
