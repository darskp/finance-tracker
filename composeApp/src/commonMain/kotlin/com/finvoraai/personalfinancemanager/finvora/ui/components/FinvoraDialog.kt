package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource

@Composable
fun FinvoraDialog(
    onDismissRequest: () -> Unit,
    showCloseIcon: Boolean = true,
    properties: DialogProperties = DialogProperties(usePlatformDefaultWidth = false),
    content: @Composable ColumnScope.() -> Unit
) {
    val palette = LocalAppPalette.current

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = Spacing.authMaxWidth)
                .fillMaxWidth()
                .padding(horizontal = Spacing.s6)
                .background(
                    color = palette.surface,
                    shape = RoundedCornerShape(Spacing.s6)
                )
                .padding(Spacing.s6)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showCloseIcon) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopEnd) {
                        IconButtonComponent(
                            painter = painterResource(Res.drawable.ic_close),
                            contentDescription = "Close",
                            onClick = onDismissRequest,
                            tint = palette.textSecondary,
                            modifier = Modifier.size(Spacing.s6)
                        )
                    }
                }

                content()
            }
        }
    }
}
