package com.finvoraai.personalfinancemanager.finvora.feature.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H1TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.chat_current_theme_label
import finvoraai.composeapp.generated.resources.chat_screen_title
import finvoraai.composeapp.generated.resources.chat_theme_dark
import finvoraai.composeapp.generated.resources.chat_theme_light
import finvoraai.composeapp.generated.resources.chat_theme_not_set
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatScreen(
    @Suppress("UnusedParameter") navController: NavController,
    viewModel: ChatViewModel = koinViewModel()
) {
    val palette = LocalAppPalette.current
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()

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
                val themeText = when (uiState.themeSetting?.isDarkMode) {
                    true -> stringResource(Res.string.chat_theme_dark)
                    false -> stringResource(Res.string.chat_theme_light)
                    null -> stringResource(Res.string.chat_theme_not_set)
                }

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
                        .padding(Spacing.s4)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(Res.string.chat_current_theme_label),
                            style = H4TextStyle(),
                            color = palette.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(Spacing.s2))

                        Text(
                            text = themeText,
                            style = BodyLarge(),
                            color = palette.textPrimary
                        )
                    }
                }
            }
        }
    }
}
