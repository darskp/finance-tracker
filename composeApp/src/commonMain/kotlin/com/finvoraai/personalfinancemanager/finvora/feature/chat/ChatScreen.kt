package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H1TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.btn_cancel
import finvoraai.composeapp.generated.resources.chat_screen_title
import finvoraai.composeapp.generated.resources.current_theme_label
import finvoraai.composeapp.generated.resources.select_theme_title
import finvoraai.composeapp.generated.resources.theme_dark
import finvoraai.composeapp.generated.resources.theme_light
import finvoraai.composeapp.generated.resources.theme_system
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(@Suppress("UnusedParameter") navController: NavController, viewModel: ChatViewModel = koinViewModel()) {
    val palette = LocalAppPalette.current
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()

    var showThemeDialog by remember { mutableStateOf(false) }

    AppBackgroundScreen {
        Column(
            modifier = Modifier
                .widthIn(max = Spacing.authMaxWidth)
                .fillMaxSize()
                .padding(horizontal = Spacing.s4),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(Spacing.s20))

            Text(
                text = stringResource(Res.string.chat_screen_title),
                style = H1TextStyle(),
                color = palette.textPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(Spacing.s6))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = palette.primary)
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Spacing.s3))
                        .background(palette.surfaceVariant)
                        .border(
                            width = Spacing.hairline,
                            color = palette.outline,
                            shape = RoundedCornerShape(Spacing.s3)
                        )
                        .clickable { showThemeDialog = true }
                        .padding(Spacing.s4)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.current_theme_label),
                            style = H4TextStyle(),
                            color = palette.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(Spacing.s2))

                        Text(
                            text = stringResource(uiState.currentThemeLabelRes),
                            style = BodyLarge(),
                            color = palette.textPrimary
                        )
                    }
                }
            }
        }
    }

    if (showThemeDialog) {
        ThemeSelectorDialog(
            currentTheme = uiState.currentThemeKey,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { selectedTheme ->
                viewModel.updateTheme(selectedTheme)
                showThemeDialog = false
            }
        )
    }
}

@Composable
fun ThemeSelectorDialog(currentTheme: String, onDismiss: () -> Unit, onThemeSelected: (String) -> Unit) {
    val palette = LocalAppPalette.current

    // Map of internal keys to their UI string resources
    val themes = listOf(
        "Light" to Res.string.theme_light,
        "Dark" to Res.string.theme_dark,
        "System" to Res.string.theme_system
    )

    AlertDialog(
        containerColor = palette.surface,
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.select_theme_title),
                style = H4TextStyle(),
                color = palette.textPrimary
            )
        },
        text = {
            Column {
                themes.forEach { (themeKey, stringRes) ->
                    val isSelected = themeKey.lowercase() == currentTheme.lowercase()
                    TextButton(
                        onClick = { onThemeSelected(themeKey) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(stringRes),
                                style = BodyLarge(),
                                color = if (isSelected) palette.primary else palette.textPrimary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(Res.string.btn_cancel),
                    style = BodyNormal(),
                    color = palette.textSecondary
                )
            }
        }
    )
}
