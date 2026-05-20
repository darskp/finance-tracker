package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H5TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.action_try_again
import finvoraai.composeapp.generated.resources.error_title_connection
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorStateView(errorMessage: StringResource, onRetry: () -> Unit, modifier: Modifier = Modifier.fillMaxSize()) {
    val palette = LocalAppPalette.current

    Column(
        modifier = modifier.padding(Spacing.s6),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(Res.string.error_title_connection),
            style = H5TextStyle().copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.s2))

        Text(
            text = stringResource(errorMessage),
            style = BodySmall().copy(
                color = palette.textSecondary,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = Spacing.s4)
        )

        Spacer(modifier = Modifier.height(Spacing.s10))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.surfaceMedium,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = RoundedCornerShape(Spacing.s6),
            contentPadding = PaddingValues(horizontal = Spacing.s10, vertical = Spacing.s3)
        ) {
            Text(
                text = stringResource(Res.string.action_try_again),
                style = BodyNormal().copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
