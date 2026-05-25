package com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.NavItem
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.util.RenderIcon
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.util.bounceClickable
import org.jetbrains.compose.resources.stringResource

@Composable
fun BottomNavItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    iconSize: Dp = 24.dp,
    textSize: TextUnit = 11.sp,
    showLabel: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .bounceClickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedVisibility(
                visible = !isSelected,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                val labelText = stringResource(item.label)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RenderIcon(
                        iconSource = item.icon,
                        contentDescription = labelText,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )

                    if (showLabel) {
                        Text(
                            text = labelText,
                            fontSize = textSize,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}
