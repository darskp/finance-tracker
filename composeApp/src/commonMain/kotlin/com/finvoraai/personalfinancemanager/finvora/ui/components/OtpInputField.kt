package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H2TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing

@Composable
fun OtpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    length: Int = 6,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val palette = LocalAppPalette.current

    BasicTextField(
        value = value,
        onValueChange = {
            val digits = it.filter { char -> char.isDigit() }
            if (digits.length <= length) {
                onValueChange(digits)
            }
        },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(Color.Transparent),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.s2),
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(length) { index ->
                    val char = when {
                        index < value.length -> value[index].toString()
                        else -> ""
                    }
                    val isFocused = value.length == index
                    val borderColor = when {
                        isError -> palette.error
                        isFocused -> palette.primary
                        else -> palette.outline
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(
                                color = palette.surface,
                                shape = RoundedCornerShape(Spacing.s3)
                            )
                            .border(
                                width = Spacing.hairline,
                                color = borderColor,
                                shape = RoundedCornerShape(Spacing.s3)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = H2TextStyle(),
                            color = palette.textPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}
