package com.finvoraai.personalfinancemanager.finvora.feature.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthEvent
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppCard
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.CommonTopAppBar
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.components.ThemeSelectorDialog
import com.finvoraai.personalfinancemanager.finvora.ui.theme.*
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.HSpacer
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.VSpacer
import finvoraai.composeapp.generated.resources.*
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    onNavigateToAuth: () -> Unit = {},
    viewModel: ProfileViewModel = koinViewModel()
) {
    val palette = LocalAppPalette.current
    val uiState by viewModel.uiState.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        ThemeSelectorDialog(
            currentTheme = uiState.currentThemeKey,
            onDismiss = { showThemeDialog = false },
            onThemeSelected = { selectedTheme ->
                viewModel.updateTheme(selectedTheme)
                showThemeDialog = false
            }
        )
    }

    AppBackgroundScreen {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                CommonTopAppBar(
                    navController = navController,
                    title = stringResource(Res.string.profile_title),
                    showBackButton = true
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.s4, vertical = Spacing.s4),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Info
                Box(
                    modifier = Modifier
                        .size(Spacing.s18)
                        .clip(CircleShape)
                        .border(Spacing.hairline, palette.outline, CircleShape)
                        .background(palette.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_avatar_placeholder),
                        contentDescription = "Profile Picture",
                        tint = palette.textSecondary,
                        modifier = Modifier.fillMaxSize().padding(Spacing.s4)
                    )
                }
                
                VSpacer(Spacing.s3)
                Text(
                    text = uiState.name ?: stringResource(Res.string.profile_dummy_name),
                    style = H5TextStyle().copy(fontWeight = FontWeight.Bold),
                    color = palette.textPrimary
                )
                VSpacer(Spacing.s1)
                Text(
                    text = uiState.email ?: stringResource(Res.string.profile_dummy_email),
                    style = BodyNormal(),
                    color = palette.textSecondary
                )
                
                VSpacer(Spacing.s6)

                // ACCOUNT & SECURITY Section
                SectionTitle(stringResource(Res.string.profile_section_account_security))
                VSpacer(Spacing.s2)
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        ProfileListItem(
                            iconRes = Res.drawable.ic_person,
                            title = stringResource(Res.string.profile_item_personal_info),
                            value = stringResource(Res.string.profile_coming_soon),
                            onClick = { /* dummy */ }
                        )
                        Divider(color = palette.outline.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = Spacing.s4))
                        ProfileListItem(
                            iconRes = Res.drawable.ic_lock,
                            title = stringResource(Res.string.profile_item_privacy_security),
                            value = stringResource(Res.string.profile_coming_soon),
                            onClick = { /* dummy */ }
                        )
                        Divider(color = palette.outline.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = Spacing.s4))
                        ProfileListItem(
                            iconRes = Res.drawable.ic_features, // fallback
                            title = stringResource(Res.string.profile_item_notifications),
                            value = stringResource(Res.string.profile_coming_soon),
                            onClick = { /* dummy */ }
                        )
                    }
                }

                VSpacer(Spacing.s6)

                // PREFERENCES Section
                SectionTitle(stringResource(Res.string.profile_section_preferences))
                VSpacer(Spacing.s2)
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        ProfileListItem(
                            iconRes = Res.drawable.ic_settings,
                            title = stringResource(Res.string.profile_item_language_region),
                            value = stringResource(Res.string.profile_coming_soon),
                            onClick = { /* dummy */ }
                        )
                        Divider(color = palette.outline.copy(alpha = 0.5f), modifier = Modifier.padding(horizontal = Spacing.s4))
                        ProfileListItem(
                            iconRes = Res.drawable.ic_visibility, // fallback for theme
                            title = stringResource(Res.string.profile_item_theme),
                            value = stringResource(uiState.currentThemeLabelRes),
                            onClick = { showThemeDialog = true }
                        )
                    }
                }

                VSpacer(Spacing.s6)

                // Logout Button
                FinvoraButton(
                    text = stringResource(Res.string.auth_logout),
                    onClick = {
                        isLoggingOut = true
                        viewModel.signOut {
                            isLoggingOut = false
                            onNavigateToAuth()
                        }
                    },
                    style = ButtonStyle.SECONDARY,
                    enabled = !isLoggingOut,
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = painterResource(Res.drawable.ic_logout),
                    leadingIconTint = palette.error,
                    textStyle = BodyNormal().copy(color = palette.error, fontWeight = FontWeight.Bold)
                )
                
                VSpacer(Spacing.s6)
                
                Text(
                    text = stringResource(Res.string.profile_version),
                    style = BodySmall(),
                    color = palette.textTertiary
                )
                VSpacer(Spacing.s6)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    val palette = LocalAppPalette.current
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = BodySmall().copy(fontWeight = FontWeight.SemiBold),
            color = palette.textTertiary
        )
    }
}

@Composable
private fun ProfileListItem(
    iconRes: DrawableResource,
    title: String,
    value: String? = null,
    onClick: () -> Unit
) {
    val palette = LocalAppPalette.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = Spacing.s4, vertical = Spacing.s3),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = title,
            tint = palette.primary,
            modifier = Modifier.size(Spacing.s5)
        )
        HSpacer(Spacing.s3)
        Text(
            text = title,
            style = BodyNormal().copy(fontWeight = FontWeight.Medium),
            color = palette.textPrimary,
            modifier = Modifier.weight(1f)
        )
        if (value != null) {
            Text(
                text = value,
                style = BodySmall(),
                color = palette.textSecondary
            )
            HSpacer(Spacing.s2)
        }
        Icon(
            painter = painterResource(Res.drawable.ic_arrow_forward),
            contentDescription = null,
            tint = palette.textTertiary,
            modifier = Modifier.size(Spacing.s4)
        )
    }
}
