package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Dialog
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H6TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.select_theme_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ThemeSelectorDialog(
    currentTheme: String,
    onDismiss: () -> Unit,
    onThemeSelected: (String) -> Unit
) {
    val palette = LocalAppPalette.current
    val themes = listOf("System", "Light", "Dark", "Ocean")

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(Spacing.s4))
                .background(palette.surface)
                .padding(Spacing.s4)
        ) {
            Text(
                text = stringResource(Res.string.select_theme_title),
                style = H6TextStyle(),
                color = palette.textPrimary,
                modifier = Modifier.padding(bottom = Spacing.s4)
            )

            themes.forEach { theme ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onThemeSelected(theme) }
                        .padding(vertical = Spacing.s2)
                ) {
                    RadioButton(
                        selected = currentTheme.equals(theme, ignoreCase = true),
                        onClick = { onThemeSelected(theme) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = palette.primary,
                            unselectedColor = palette.textSecondary
                        )
                    )
                    Spacer(modifier = Modifier.width(Spacing.s2))
                    Text(
                        text = theme,
                        style = BodyNormal(),
                        color = palette.textPrimary
                    )
                }
            }
        }
    }
}
