package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.ic_visibility
import finvoraai.composeapp.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource

private const val SHAKE_DURATION = 300
private const val SHAKE_OFFSET_LARGE = 10f
private const val SHAKE_OFFSET_MEDIUM = 5f
private const val SHAKE_OFFSET_SMALL = 2f
private const val SHAKE_TIME_1 = 50
private const val SHAKE_TIME_2 = 100
private const val SHAKE_TIME_3 = 150
private const val SHAKE_TIME_4 = 200
private const val SHAKE_TIME_5 = 250

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    isPassword: Boolean = false,
    leadingIcon: ImageVector? = null,
    leadingIconPainter: Painter? = null,
    trailingIcon: ImageVector? = null,
    shape: Shape = RoundedCornerShape(Spacing.s3),
    singleLine: Boolean = true,
    textStyle: TextStyle = BodyLarge().copy(
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colorScheme.onSurface
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
    errorColor: Color = MaterialTheme.colorScheme.error,
    contentPadding: PaddingValues = OutlinedTextFieldDefaults.contentPadding(),
    fieldWidth: androidx.compose.ui.unit.Dp? = null,
    fieldHeight: androidx.compose.ui.unit.Dp = Spacing.s13
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val shakeOffset = remember { androidx.compose.animation.core.Animatable(0f) }
    LaunchedEffect(isError) {
        if (isError) {
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = androidx.compose.animation.core.keyframes {
                    durationMillis = SHAKE_DURATION
                    -SHAKE_OFFSET_LARGE at SHAKE_TIME_1
                    SHAKE_OFFSET_LARGE at SHAKE_TIME_2
                    -SHAKE_OFFSET_MEDIUM at SHAKE_TIME_3
                    SHAKE_OFFSET_MEDIUM at SHAKE_TIME_4
                    -SHAKE_OFFSET_SMALL at SHAKE_TIME_5
                }
            )
        }
    }

    Column(modifier = modifier.then(Modifier.offset(x = shakeOffset.value.dp))) {
        if (label != null) {
            Text(
                text = label,
                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                color = textColor.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = Spacing.s2)
            )
        }

        val colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor,
            errorBorderColor = errorColor,
            focusedLabelColor = focusedBorderColor,
            cursorColor = cursorColor,
            errorCursorColor = errorColor,
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor
        )

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .then(if (fieldWidth != null) Modifier.width(fieldWidth) else Modifier.fillMaxWidth())
                .height(fieldHeight)
                .clip(shape),
            interactionSource = interactionSource,
            enabled = enabled,
            singleLine = singleLine,
            textStyle = textStyle.copy(color = textColor),
            keyboardOptions = if (isPassword) {
                keyboardOptions.copy(keyboardType = KeyboardType.Password)
            } else {
                keyboardOptions
            },
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(cursorColor),
            decorationBox = { innerTextField ->
                OutlinedTextFieldDefaults.DecorationBox(
                    value = value,
                    innerTextField = innerTextField,
                    enabled = enabled,
                    singleLine = singleLine,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            text = placeholder,
                            style = BodyNormal().copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            )
                        )
                    },
                    leadingIcon = when {
                        leadingIconPainter != null -> {
                            {
                                Icon(
                                    painter = leadingIconPainter,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(Spacing.s5)
                                )
                            }
                        }
                        leadingIcon != null -> {
                            {
                                Icon(
                                    imageVector = leadingIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(Spacing.s5)
                                )
                            }
                        }
                        else -> null
                    },
                    trailingIcon = {
                        when {
                            isPassword -> {
                                val visibilityIcon =
                                    if (passwordVisible) {
                                        painterResource(
                                            Res.drawable.ic_visibility_off
                                        )
                                    } else {
                                        painterResource(Res.drawable.ic_visibility)
                                    }
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = visibilityIcon,
                                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(Spacing.s5)
                                    )
                                }
                            }

                            trailingIcon != null -> {
                                Icon(
                                    imageVector = trailingIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(Spacing.s5)
                                )
                            }

                            else -> null
                        }
                    },
                    colors = colors,
                    container = {
                        OutlinedTextFieldDefaults.Container(
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            colors = colors,
                            shape = shape
                        )
                    },
                    contentPadding = PaddingValues(horizontal = Spacing.s3Half, vertical = Spacing.default)
                )
            }
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = errorColor,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = Spacing.s4, top = Spacing.s1)
            )
        }
    }
}
