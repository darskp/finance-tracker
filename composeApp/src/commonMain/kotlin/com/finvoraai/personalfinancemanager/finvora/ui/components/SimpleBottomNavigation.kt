package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.IconSource
import com.finvoraai.personalfinancemanager.finvora.ui.animatedBottomBar.models.NavItem
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleBottomNavigation(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    items: List<NavItem>,
    onItemSelected: (Int) -> Unit,
    height: Dp = 70.dp
) {
    val palette = LocalAppPalette.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .background(palette.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            key(item.route) {
                SimpleNavItemContent(
                    item = item,
                    isSelected = index == selectedIndex,
                    onClick = { onItemSelected(index) }
                )
            }
        }
    }
}

@Composable
private fun SimpleNavItemContent(item: NavItem, isSelected: Boolean, onClick: () -> Unit) {
    val palette = LocalAppPalette.current
    val interactionSource = remember { MutableInteractionSource() }
    val labelText = stringResource(item.label)

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(Spacing.s5))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .then(
                if (isSelected) {
                    Modifier
                        .background(palette.primary.copy(alpha = 0.1f))
                        .padding(horizontal = Spacing.s5, vertical = Spacing.s2)
                } else {
                    Modifier.padding(horizontal = Spacing.s4, vertical = Spacing.s2)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RenderSimpleIcon(
                icon = item.icon,
                contentDescription = labelText,
                tint = if (isSelected) palette.primary else palette.textSecondary,
                modifier = Modifier.size(Spacing.s6)
            )

            if (isSelected) {
                Spacer(modifier = Modifier.size(Spacing.s2))
                Text(
                    text = labelText,
                    style = BodyNormal().copy(
                        color = palette.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun RenderSimpleIcon(
    icon: IconSource,
    contentDescription: String?,
    tint: Color,
    modifier: Modifier = Modifier
) {
    when (icon) {
        is IconSource.Vector -> {
            Icon(
                imageVector = icon.imageVector,
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
        is IconSource.Drawable -> {
            Icon(
                painter = painterResource(icon.resId),
                contentDescription = contentDescription,
                tint = tint,
                modifier = modifier
            )
        }
    }
}
