package com.finvoraai.personalfinancemanager.finvora.feature.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import com.finvoraai.personalfinancemanager.finvora.feature.chat.ThemeSelectorDialog
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyXSmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H1TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Opacity
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.StaticColors
import com.finvoraai.personalfinancemanager.finvora.ui.utils.WelcomeImageProvider
import com.finvoraai.personalfinancemanager.finvora.ui.utils.collectAsStateLifecycleAware
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Suppress("UnusedParameter")
@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit = {},
    onViewDemoClick: () -> Unit = {},
    viewModel: WelcomeViewModel = koinViewModel()
) {
    val palette = LocalAppPalette.current
    val welcomeImage = remember { WelcomeImageProvider.getRandomImage() }
    val uiState by viewModel.uiState.collectAsStateLifecycleAware()
    var showThemeDialog by remember { mutableStateOf(false) }

    AppBackgroundScreen {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = Spacing.authMaxWidth)
                    .padding(horizontal = Spacing.s6),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.s24))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(Spacing.AUTH_LOGO_WIDTH_FRACTION)
                        .aspectRatio(1f)
                        .shadow(
                            elevation = Spacing.s4,
                            shape = RoundedCornerShape(Spacing.s3),
                            clip = false,
                            ambientColor = StaticColors.Black.copy(alpha = Opacity.SHADOW_AMBIENT),
                            spotColor = StaticColors.Black.copy(alpha = Opacity.SHADOW_AMBIENT)
                        )
                        .clip(RoundedCornerShape(Spacing.s3))
                        .background(palette.surface)
                        .border(Spacing.hairline, palette.outline, RoundedCornerShape(Spacing.s3))
                ) {
                    Image(
                        painter = painterResource(welcomeImage.resource),
                        contentDescription = stringResource(Res.string.cd_logo),
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        StaticColors.Transparent,
                                        palette.background.copy(alpha = Opacity.WELCOME_SCRIM_OPACITY)
                                    ),
                                    startY = Spacing.GRADIENT_START_Y
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s12))

                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(palette.surfaceVariant.copy(alpha = Opacity.BADGE_BG_OPACITY))
                        .border(Spacing.hairline, palette.outline, CircleShape)
                        .padding(horizontal = Spacing.s3, vertical = Spacing.s1),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s2)
                ) {
                    Box(
                        modifier = Modifier
                            .size(Spacing.s2)
                            .background(palette.primary, CircleShape)
                    )
                    Text(
                        text = stringResource(Res.string.welcome_version_badge),
                        style = BodyXSmall(),
                        color = palette.textTertiary
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s4))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = palette.textPrimary)) {
                            append(stringResource(Res.string.welcome_heading_start))
                            append("\n")
                        }
                        withStyle(SpanStyle(color = palette.primary)) {
                            append(stringResource(Res.string.welcome_heading_brand))
                        }
                    },
                    style = H1TextStyle(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.s4))

                Text(
                    text = stringResource(Res.string.welcome_description),
                    style = BodyNormal(),
                    color = palette.textSecondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = Spacing.s4)
                )

                Spacer(modifier = Modifier.height(Spacing.s16))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.s30),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.s4)
                ) {
                    FinvoraButton(
                        text = stringResource(Res.string.welcome_btn_get_started),
                        onClick = onGetStartedClick,
                        style = ButtonStyle.PRIMARY,
                        modifier = Modifier.widthIn(max = Spacing.s100)
                    )
                }
            }

            // Theme selector at the top-right, taking 30% width
            if (!uiState.isLoading) {
                WelcomeThemeSelector(
                    currentThemeLabelRes = uiState.currentThemeLabelRes,
                    onClick = { showThemeDialog = true },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = Spacing.s6, end = Spacing.s6)
                )
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

private const val THEME_SELECTOR_WIDTH_FRACTION = 0.3f

@Composable
fun WelcomeThemeSelector(currentThemeLabelRes: StringResource, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val palette = LocalAppPalette.current

    Box(
        modifier = modifier
            .fillMaxWidth(THEME_SELECTOR_WIDTH_FRACTION)
            .clip(RoundedCornerShape(Spacing.s3))
            .background(palette.surfaceVariant)
            .border(
                width = Spacing.hairline,
                color = palette.outline,
                shape = RoundedCornerShape(Spacing.s3)
            )
            .clickable(onClick = onClick)
            .padding(Spacing.s3)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(Res.string.current_theme_label),
                style = BodyXSmall(),
                color = palette.primary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(Spacing.s1))

            Text(
                text = stringResource(currentThemeLabelRes),
                style = BodyNormal(),
                color = palette.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
