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
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.style.TextAlign
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.components.OnboardingButton
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.components.OnboardingButtonType
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H1TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Opacity
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val WEIGHT_LANDSCAPE_IMAGE = 1.1f
private const val WEIGHT_LANDSCAPE_CONTENT = 0.9f
private const val WEIGHT_PORTRAIT_IMAGE = 1.2f

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

    AppBackgroundScreen {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                // Landscape layout: Row
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = Spacing.s6, vertical = Spacing.s4),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s6),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: Mockup Image
                    Box(
                        modifier = Modifier
                            .weight(WEIGHT_LANDSCAPE_IMAGE)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = currentPage,
                            transitionSpec = {
                                val direction = if (isForward) 1 else -1
                                (
                                    slideInHorizontally(
                                        initialOffsetX = { it * direction },
                                        animationSpec = tween(
                                            Motion.DURATION_STANDARD,
                                            easing = Motion.StandardEasing
                                        )
                                    ) + fadeIn(tween(Motion.DURATION_STANDARD))
                                    ) togetherWith
                                    (
                                        slideOutHorizontally(
                                            targetOffsetX = { -it * direction },
                                            animationSpec = tween(
                                                Motion.DURATION_STANDARD,
                                                easing = Motion.StandardEasing
                                            )
                                        ) + fadeOut(tween(Motion.DURATION_FADE))
                                        )
                            },
                            label = stringResource(Res.string.onboarding_label_image_landscape)
                        ) { page ->
                            Image(
                                painter = painterResource(onboardingPages[page].image),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Right: Text, indicators, and buttons
                    Column(
                        modifier = Modifier
                            .weight(WEIGHT_LANDSCAPE_CONTENT)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(Spacing.s2))

                        AnimatedContent(
                            targetState = currentPage,
                            transitionSpec = {
                                val direction = if (isForward) 1 else -1
                                (
                                    slideInHorizontally(
                                        initialOffsetX = { (it * 0.3 * direction).toInt() },
                                        animationSpec = tween(
                                            Motion.DURATION_STANDARD,
                                            easing = Motion.StandardEasing
                                        )
                                    ) + fadeIn(tween(Motion.DURATION_STANDARD))
                                    ) togetherWith
                                    (
                                        slideOutHorizontally(
                                            targetOffsetX = { (-it * 0.3 * direction).toInt() },
                                            animationSpec = tween(
                                                Motion.DURATION_STANDARD,
                                                easing = Motion.StandardEasing
                                            )
                                        ) + fadeOut(tween(Motion.DURATION_FADE))
                                        )
                            },
                            label = stringResource(Res.string.onboarding_label_text_landscape)
                        ) { page ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(onboardingPages[page].title),
                                    color = palette.textPrimary,
                                    style = H1TextStyle(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(Spacing.s3))
                                Text(
                                    text = stringResource(onboardingPages[page].subtitle),
                                    color = palette.textSecondary,
                                    style = BodyNormal(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(Spacing.s4))

                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            repeat(totalPages) { index ->
                                OnboardingDot(isActive = index == currentPage)
                                if (index < totalPages - 1) Spacer(modifier = Modifier.width(Spacing.s2))
                            }
                        }

                        Spacer(modifier = Modifier.height(Spacing.s4))

                        OnboardingNavigationButtons(
                            currentPage = currentPage,
                            totalPages = totalPages,
                            onPrevClick = {
                                isForward = false
                                currentPage--
                            },
                            onNextClick = {
                                isForward = true
                                currentPage++
                            },
                            onExploreClick = {
                                scope.launch {
                                    onBoardingViewModel.setOnboardingCompleted(true)
                                    onOnboardingComplete()
                                }
                            }
                        )
                    }
                }
            } else {
                // Portrait layout (Phone & Tablet): Column
                Column(
                    modifier = Modifier
                        .widthIn(max = Spacing.authMaxWidth)
                        .fillMaxSize()
                        .padding(horizontal = Spacing.s6),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.height(Spacing.s4))

                    // Mockup Image
                    Box(
                        modifier = Modifier
                            .weight(WEIGHT_PORTRAIT_IMAGE)
                            .fillMaxWidth()
                            .padding(vertical = Spacing.s4),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedContent(
                            targetState = currentPage,
                            transitionSpec = {
                                val direction = if (isForward) 1 else -1
                                (
                                    slideInHorizontally(
                                        initialOffsetX = { it * direction },
                                        animationSpec = tween(
                                            Motion.DURATION_STANDARD,
                                            easing = Motion.StandardEasing
                                        )
                                    ) + fadeIn(tween(Motion.DURATION_STANDARD))
                                    ) togetherWith
                                    (
                                        slideOutHorizontally(
                                            targetOffsetX = { -it * direction },
                                            animationSpec = tween(
                                                Motion.DURATION_STANDARD,
                                                easing = Motion.StandardEasing
                                            )
                                        ) + fadeOut(tween(Motion.DURATION_FADE))
                                        )
                            },
                            label = stringResource(Res.string.onboarding_label_image_portrait)
                        ) { page ->
                            Image(
                                painter = painterResource(onboardingPages[page].image),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Spacing.s4))

                    // Text section
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
                        label = stringResource(Res.string.onboarding_label_text_portrait)
                    ) { page ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(onboardingPages[page].title),
                                color = palette.textPrimary,
                                style = H1TextStyle(),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(Spacing.s3))
                            Text(
                                text = stringResource(onboardingPages[page].subtitle),
                                color = palette.textSecondary,
                                style = BodyNormal(),
                                textAlign = TextAlign.Center
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

                    OnboardingNavigationButtons(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        onPrevClick = {
                            isForward = false
                            currentPage--
                        },
                        onNextClick = {
                            isForward = true
                            currentPage++
                        },
                        onExploreClick = {
                            scope.launch {
                                onBoardingViewModel.setOnboardingCompleted(true)
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
private fun OnboardingNavigationButtons(
    currentPage: Int,
    totalPages: Int,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onExploreClick: () -> Unit
) {
    when {
        // First page: hide PREV, right-align NEXT
        currentPage == 0 -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.s4),
                contentAlignment = Alignment.CenterEnd
            ) {
                OnboardingButton(
                    type = OnboardingButtonType.NEXT,
                    onClick = onNextClick
                )
            }
        }
        // Last page: show PREV + EXPLORE
        currentPage == totalPages - 1 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.s4),
                horizontalArrangement = Arrangement.spacedBy(Spacing.s4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OnboardingButton(
                    type = OnboardingButtonType.PREV,
                    enabled = true,
                    onClick = onPrevClick
                )

                OnboardingButton(
                    type = OnboardingButtonType.EXPLORE,
                    modifier = Modifier.weight(1f),
                    onClick = onExploreClick
                )
            }
        }
        // Middle pages: show both PREV + NEXT
        else -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Spacing.s4),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OnboardingButton(
                    type = OnboardingButtonType.PREV,
                    enabled = true,
                    onClick = onPrevClick
                )

                OnboardingButton(
                    type = OnboardingButtonType.NEXT,
                    onClick = onNextClick
                )
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
        label = stringResource(Res.string.onboarding_label_dot_width)
    )
    val alpha by animateFloatAsState(
        targetValue = if (isActive) Opacity.FULL else Opacity.DISABLED,
        animationSpec = tween(Motion.DURATION_STANDARD, easing = Motion.StandardEasing),
        label = stringResource(Res.string.onboarding_label_dot_alpha)
    )

    Box(
        modifier = Modifier
            .height(Spacing.s2)
            .width(width)
            .clip(CircleShape)
            .background(
                if (isActive) {
                    Brush.horizontalGradient(palette.gradients.onboardingNextGradient)
                } else {
                    Brush.horizontalGradient(
                        listOf(
                            palette.outline.copy(alpha = alpha),
                            palette.outline.copy(alpha = alpha)
                        )
                    )
                }
            )
    )
}
