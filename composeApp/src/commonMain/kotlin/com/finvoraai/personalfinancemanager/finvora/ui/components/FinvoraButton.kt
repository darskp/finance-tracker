package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing

enum class ButtonStyle {
    PRIMARY,
    SECONDARY,
    MINIMAL
}

@Composable
fun FinvoraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.PRIMARY,
    enabled: Boolean = true,
    textStyle: androidx.compose.ui.text.TextStyle? = null,
    buttonWidth: androidx.compose.ui.unit.Dp? = null,
    buttonHeight: androidx.compose.ui.unit.Dp? = null
) {
    val palette = LocalAppPalette.current

    val containerColor = when (style) {
        ButtonStyle.PRIMARY -> palette.primary
        ButtonStyle.SECONDARY -> palette.surfaceMedium
        ButtonStyle.MINIMAL -> palette.primary.copy(alpha = 0.1f)
    }

    val contentColor = when (style) {
        ButtonStyle.PRIMARY -> palette.onPrimary
        ButtonStyle.SECONDARY -> palette.textPrimary
        ButtonStyle.MINIMAL -> palette.primary
    }

    val buttonModifier = modifier
        .then(if (buttonWidth != null) Modifier.width(buttonWidth) else Modifier.fillMaxWidth())
        .then(
            if (buttonHeight != null) {
                Modifier.height(buttonHeight)
            } else if (style != ButtonStyle.MINIMAL) {
                Modifier.height(Spacing.s14)
            } else {
                Modifier
            }
        )
        .then(
            if (style == ButtonStyle.PRIMARY && enabled) {
                Modifier.shadow(
                    elevation = Spacing.s2,
                    shape = RoundedCornerShape(Spacing.s3),
                    spotColor = palette.primary.copy(alpha = 0.2f)
                )
            } else if (style == ButtonStyle.SECONDARY) {
                Modifier.border(1.dp, palette.outline, RoundedCornerShape(Spacing.s3))
            } else {
                Modifier
            }
        )

    Button(
        onClick = onClick,
        modifier = buttonModifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(Spacing.s3),
        contentPadding = if (style == ButtonStyle.MINIMAL) {
            PaddingValues(
                vertical = Spacing.s3
            )
        } else {
            ButtonDefaults.ContentPadding
        }
    ) {
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.titleSmall,
            color = contentColor
        )
    }
}
