package com.finvoraai.personalfinancemanager.finvora.feature.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LogoTextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Opacity
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.VSpacer
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.app_title
import finvoraai.composeapp.generated.resources.splash_tagline
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

@Composable
fun SplashScreen(onComplete: () -> Unit) {
    val palette = LocalAppPalette.current
    val appTitle = stringResource(Res.string.app_title)
    var typedText by remember { mutableStateOf("") }
    val taglineAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        appTitle.forEachIndexed { index, _ ->
            typedText = appTitle.substring(0, index + 1)
            delay(Motion.TYPING_CHAR_DELAY)
        }

        launch {
            taglineAlpha.animateTo(
                targetValue = Opacity.FULL,
                animationSpec = tween(Motion.DURATION_EMPHASIS)
            )
        }

        delay(Motion.SPLASH_EXIT_DELAY)
        onComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(palette.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = typedText,
                style = LogoTextStyle().copy(
                    color = palette.textPrimary
                )
            )

            VSpacer(Spacing.s4)

            Text(
                text = stringResource(Res.string.splash_tagline),
                style = BodySmall().copy(
                    color = palette.textSecondary.copy(alpha = Opacity.MEDIUM)
                ),
                modifier = Modifier.alpha(taglineAlpha.value)
            )
        }
    }
}
