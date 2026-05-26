package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
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
    textStyle: TextStyle? = null,
    buttonWidth: Dp? = null,
    buttonHeight: Dp? = null,
    leadingIcon: Painter? = null,
    leadingIconTint: Color? = null,
    tintIcon: Boolean = true
) {
    val palette = LocalAppPalette.current

    val containerColor = when (style) {
        ButtonStyle.PRIMARY -> palette.primary
        ButtonStyle.SECONDARY -> palette.surfaceVariant
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
        if (leadingIcon != null) {
            Icon(
                painter = leadingIcon,
                contentDescription = null,
                tint = if (tintIcon) (leadingIconTint ?: contentColor) else Color.Unspecified,
                modifier = Modifier.size(Spacing.s5)
            )
            Spacer(modifier = Modifier.width(Spacing.s2))
        }
        Text(
            text = text,
            style = textStyle ?: MaterialTheme.typography.titleSmall,
            color = contentColor
        )
    }
}
