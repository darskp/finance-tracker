package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing

@Composable
fun AppElevatedCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .bounceClickable(onClick = onClick)
            .padding(vertical = Spacing.s2)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(Spacing.s4),
                ambientColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                spotColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
            .clip(RoundedCornerShape(Spacing.s4))
            .background(MaterialTheme.colorScheme.surface)
            .padding(Spacing.s4)
    ) {
        content()
    }
}
