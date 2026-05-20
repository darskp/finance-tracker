package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import com.finvoraai.personalfinancemanager.finvora.ui.theme.*
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.HSpacer
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.app_title
import finvoraai.composeapp.generated.resources.cd_back
import finvoraai.composeapp.generated.resources.cd_logo
import finvoraai.composeapp.generated.resources.finvoraai_logo_no_text
import finvoraai.composeapp.generated.resources.ic_arrow_back
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * A unified TopAppBar that can be used across different screens.
 * Supports back button, leading image (URL or Resource), title, subtitle, and custom actions.
 */
@Composable
fun CommonTopAppBar(
    navController: NavController,
    title: String,
    subtitle: String? = null,
    showBackButton: Boolean = true,
    leadingImage: Any? = null,
    leadingIconTint: Color = Color.Unspecified,
    modifier: Modifier = Modifier,
    actions: @Composable (RowScope.() -> Unit)? = null
) {
    val palette = LocalAppPalette.current
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .height(56.dp)
                .padding(horizontal = Spacing.s2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            if (showBackButton) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.cd_back),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                HSpacer(Spacing.s4)
            }

            // Leading Image/Icon
            if (leadingImage != null) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            when (leadingImage) {
                                is ImageVector -> palette.accent.copy(alpha = 0.2f)
                                is DrawableResource -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    when (leadingImage) {
                        is ImageVector -> {
                            Icon(
                                imageVector = leadingImage,
                                contentDescription = null,
                                tint = palette.accent,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        is DrawableResource -> {
                            Icon(
                                painter = painterResource(leadingImage),
                                contentDescription = null,
                                tint = leadingIconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        else -> {
                            val placeholderContent: @Composable () -> Unit = {
                                Text(
                                    text = title.firstOrNull()?.toString()?.uppercase() ?: "?",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                            SubcomposeAsyncImage(
                                model = leadingImage,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                success = {
                                    SubcomposeAsyncImageContent()
                                },
                                loading = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) { placeholderContent() }
                                },
                                error = {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.fillMaxSize()
                                    ) { placeholderContent() }
                                }
                            )
                        }
                    }
                }
                HSpacer(Spacing.s3)
            }

            // Title & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = BodyXLarge().copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = BodySmall().copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Actions
            if (actions != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            } else {
                HSpacer(Spacing.s4)
            }
        }
    }
}

/**
 * Specific variant for the Home screen which often has a unique logo placement.
 */
@Suppress("UnusedParameter")
@Composable
fun HomeTopAppBar(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .height(56.dp)
                .padding(horizontal = Spacing.s4),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // App Identity
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(Res.drawable.finvoraai_logo_no_text),
                    contentDescription = stringResource(Res.string.cd_logo),
                    modifier = Modifier.size(32.dp),
                    tint = Color.Unspecified
                )
                HSpacer(Spacing.s2)
                Text(
                    text = stringResource(Res.string.app_title),
                    style = LogoTextStyle().copy(
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
