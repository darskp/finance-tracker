package com.finvoraai.personalfinancemanager.finvora.feature.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.components.OnboardingButton
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.components.OnboardingButtonType
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H1TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Opacity
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.StaticColors
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

data class OnboardingPage(
    val image: DrawableResource,
    val title: StringResource,
    val subtitle: StringResource
)

private val onboardingPages = listOf(
    OnboardingPage(
        image = Res.drawable.onboarding_temp1,
        title = Res.string.onboarding_page1_title,
        subtitle = Res.string.onboarding_page1_subtitle
    ),
    OnboardingPage(
        image = Res.drawable.onboarding_temp2,
        title = Res.string.onboarding_page2_title,
        subtitle = Res.string.onboarding_page2_subtitle
    ),
    OnboardingPage(
        image = Res.drawable.onboarding_temp3,
        title = Res.string.onboarding_page3_title,
        subtitle = Res.string.onboarding_page3_subtitle
    )
)

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit = {},
    onBoardingViewModel: OnBoardingViewModel = koinViewModel()
) {
    val palette = LocalAppPalette.current
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableIntStateOf(0) }
    var isForward by remember { mutableStateOf(true) }
    val totalPages = onboardingPages.size

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                val direction = if (isForward) 1 else -1
                (
                    slideInHorizontally(
                        initialOffsetX = { it * direction },
                        animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing)
                    ) + fadeIn(tween(Motion.DURATION_STANDARD))
                    ) togetherWith
                    (
                        slideOutHorizontally(
                            targetOffsetX = { -it * direction },
                            animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing)
                        ) + fadeOut(tween(Motion.DURATION_FADE))
                        )
            },
            label = "onboarding_image"
        ) { page ->
            Image(
                painter = painterResource(onboardingPages[page].image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to StaticColors.Transparent,
                            Opacity.GRADIENT_STOP_START to StaticColors.Black.copy(alpha = Opacity.SCRIM_LIGHT),
                            Opacity.GRADIENT_STOP_END to StaticColors.Black.copy(alpha = Opacity.SCRIM_MEDIUM),
                            1.0f to StaticColors.Black.copy(alpha = Opacity.SCRIM_DARK)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .widthIn(max = Spacing.authMaxWidth)
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.s7)
                .align(Alignment.Center),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    val direction = if (isForward) 1 else -1
                    (
                        slideInHorizontally(
                            initialOffsetX = { (it * 0.3 * direction).toInt() },
                            animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing)
                        ) + fadeIn(tween(Motion.DURATION_STANDARD))
                        ) togetherWith
                        (
                            slideOutHorizontally(
                                targetOffsetX = { (-it * 0.3 * direction).toInt() },
                                animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing)
                            ) + fadeOut(tween(Motion.DURATION_FADE))
                            )
                },
                label = "onboarding_text"
            ) { page ->
                Column {
                    Text(
                        text = stringResource(onboardingPages[page].title),
                        color = palette.textPrimary,
                        style = H1TextStyle()
                    )
                    Spacer(modifier = Modifier.height(Spacing.s3))
                    Text(
                        text = stringResource(onboardingPages[page].subtitle),
                        color = palette.textSecondary,
                        style = BodyNormal()
                    )
                }
            }

            Spacer(modifier = Modifier.height(Spacing.s8))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(totalPages) { index ->
                    OnboardingDot(isActive = index == currentPage)
                    if (index < totalPages - 1) Spacer(modifier = Modifier.width(Spacing.s2))
                }
            }

            Spacer(modifier = Modifier.height(Spacing.s7))

            if (currentPage < totalPages - 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.s3),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OnboardingButton(
                        type = OnboardingButtonType.PREV,
                        enabled = currentPage > 0,
                        onClick = {
                            if (currentPage > 0) {
                                isForward = false
                                currentPage--
                            }
                        }
                    )

                    OnboardingButton(
                        type = OnboardingButtonType.NEXT,
                        onClick = {
                            isForward = true
                            currentPage++
                        }
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = Spacing.s3),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OnboardingButton(
                        type = OnboardingButtonType.PREV,
                        enabled = true,
                        onClick = {
                            isForward = false
                            currentPage--
                        }
                    )

                    OnboardingButton(
                        type = OnboardingButtonType.EXPLORE,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                onBoardingViewModel.setUserStatus(false)
                                onOnboardingComplete()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingDot(isActive: Boolean) {
    val palette = LocalAppPalette.current
    val width by animateDpAsState(
        targetValue = if (isActive) Spacing.s7 else Spacing.s2,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "dot_width"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isActive) Opacity.FULL else Opacity.DISABLED,
        animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing),
        label = "dot_alpha"
    )

    Box(
        modifier = Modifier
            .height(Spacing.s2)
            .width(width)
            .clip(CircleShape)
            .background(
                if (isActive) {
                    Brush.horizontalGradient(palette.onboardingNextGradient)
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            StaticColors.White.copy(alpha = alpha),
                            StaticColors.White.copy(alpha = alpha)
                        )
                    )
                }
            )
    )
}
