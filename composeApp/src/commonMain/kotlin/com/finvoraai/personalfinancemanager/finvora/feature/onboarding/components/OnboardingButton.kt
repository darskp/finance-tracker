package com.finvoraai.personalfinancemanager.finvora.feature.onboarding.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H6TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Opacity
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

enum class OnboardingButtonType {
    PREV,
    NEXT,
    EXPLORE
}

@Composable
fun OnboardingButton(
    type: OnboardingButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val palette = LocalAppPalette.current

    val scale by animateFloatAsState(
        targetValue = if (isPressed) Opacity.BUTTON_PRESSED_SCALE else Opacity.FULL,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "scale"
    )

    when (type) {
        OnboardingButtonType.PREV -> {
            Box(
                modifier = modifier
                    .scale(scale)
                    .size(Spacing.s13)
                    .clip(CircleShape)
                    .border(
                        width = Spacing.borderMedium,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = Opacity.TRANSPARENT_BORDER),
                        shape = CircleShape
                    )
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = Opacity.TRANSPARENT_BG))
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = enabled,
                        onClick = onClick
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.onboarding_btn_prev),
                    tint = androidx.compose.ui.graphics.Color.White.copy(
                        alpha = if (enabled) Opacity.ICON_ENABLED else Opacity.ICON_DISABLED
                    ),
                    modifier = Modifier.size(Spacing.s5)
                )
            }
        }

        OnboardingButtonType.NEXT -> {
            Box(
                modifier = modifier
                    .scale(scale)
                    .height(Spacing.s13)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = palette.onboardingNextGradient
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                    .padding(horizontal = Spacing.s7),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.onboarding_btn_next),
                        color = palette.onPrimary,
                        style = BodyLarge().copy(
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
                        )
                    )
                    Spacer(modifier = Modifier.width(Spacing.s2))
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_forward),
                        contentDescription = null,
                        tint = palette.onPrimary,
                        modifier = Modifier.size(Spacing.iconMedium)
                    )
                }
            }
        }

        OnboardingButtonType.EXPLORE -> {
            Box(
                modifier = modifier
                    .scale(scale)
                    .height(Spacing.s14)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = palette.onboardingExploreGradient
                        )
                    )
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = onClick
                    )
                    .padding(horizontal = Spacing.s10),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_btn_explore),
                    color = palette.onPrimary,
                    style = H6TextStyle().copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
