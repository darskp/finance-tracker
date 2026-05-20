package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun IconButtonComponent(
    painter: Painter,
    onClick: () -> Unit,
    contentDescription: String? = null,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.onSurface
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painter,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}
