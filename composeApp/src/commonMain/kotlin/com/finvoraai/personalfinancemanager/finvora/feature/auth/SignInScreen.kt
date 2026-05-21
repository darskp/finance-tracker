package com.finvoraai.personalfinancemanager.finvora.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppBackgroundScreen
import com.finvoraai.personalfinancemanager.finvora.ui.components.AppTextField
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.components.IconButtonComponent
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodySmall
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H2TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignInScreen(
    onSignInSuccess: () -> Unit = {},
    onNavigateToSignUp: () -> Unit = {},
    onNavigateToForgotPassword: () -> Unit = {},
    viewModel: AuthViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState is AuthUiState.Loading
    val apiError = (uiState as? AuthUiState.Error)?.message
    val displayError = validationError ?: apiError

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.NavigateToDashboard -> onSignInSuccess()
            }
        }
    }

    val palette = LocalAppPalette.current

    AppBackgroundScreen(showTopCenterBlur = true) {
        Box(
            modifier = Modifier.fillMaxSize().imePadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = Spacing.authMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.s6),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(Spacing.s20))

                Box(
                    modifier = Modifier
                        .size(Spacing.s16),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.finvoraai_logo_no_text),
                        contentDescription = stringResource(Res.string.cd_logo),
                        modifier = Modifier.size(Spacing.s12)
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s1))

                Text(
                    text = stringResource(Res.string.auth_app_name),
                    style = H2TextStyle(),
                    color = palette.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.s2))

                Text(
                    text = stringResource(Res.string.auth_sign_in_subtitle),
                    style = BodyLarge(),
                    color = palette.textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(Spacing.s8))

                AnimatedVisibility(
                    visible = displayError != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = Spacing.s4)
                            .background(
                                color = palette.error.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(Spacing.s3)
                            )
                            .border(
                                width = Spacing.hairline,
                                color = palette.error.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(Spacing.s3)
                            )
                            .padding(horizontal = Spacing.s4, vertical = Spacing.s3),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = displayError ?: "",
                            color = palette.error,
                            style = BodyNormal(),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButtonComponent(
                            painter = painterResource(Res.drawable.ic_close),
                            onClick = {
                                validationError = null
                                viewModel.resetState()
                            },
                            contentDescription = stringResource(Res.string.cd_dismiss_error),
                            tint = palette.error,
                            modifier = Modifier.size(Spacing.s4)
                        )
                    }
                }

                AppTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    label = stringResource(Res.string.auth_email_label),
                    placeholder = stringResource(Res.string.auth_email_placeholder),
                    leadingIconPainter = painterResource(Res.drawable.ic_email),
                    isError = emailError
                )

                Spacer(modifier = Modifier.height(Spacing.s4))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.auth_password_label),
                        style = BodyNormal(),
                        color = palette.textPrimary,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = stringResource(Res.string.auth_forgot),
                        style = BodyNormal(),
                        color = palette.primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            validationError = null
                            onNavigateToForgotPassword()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s2))

                AppTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    label = null,
                    placeholder = stringResource(Res.string.auth_password_placeholder),
                    isPassword = true,
                    leadingIconPainter = painterResource(Res.drawable.ic_lock),
                    isError = passwordError
                )

                Spacer(modifier = Modifier.height(Spacing.s6))

                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = palette.primary,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(Spacing.s6)
                        )
                    }
                } else {
                    FinvoraButton(
                        text = stringResource(Res.string.auth_sign_in_btn),
                        onClick = {
                            validationError = null
                            emailError = false
                            passwordError = false

                            val emailVal = AuthValidator.validateEmail(email)
                            if (emailVal is ValidationResult.Failure) {
                                validationError = emailVal.message
                                emailError = true
                            } else {
                                val passwordVal = AuthValidator.validatePassword(password)
                                if (passwordVal is ValidationResult.Failure) {
                                    validationError = passwordVal.message
                                    passwordError = true
                                } else {
                                    viewModel.signIn(email, password)
                                }
                            }
                        },
                        style = ButtonStyle.PRIMARY,
                        enabled = !isLoading
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s6))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(Spacing.hairline)
                            .background(palette.outline)
                    )
                    Text(
                        text = stringResource(Res.string.auth_or),
                        style = BodySmall(),
                        color = palette.textSecondary,
                        modifier = Modifier.padding(horizontal = Spacing.s4)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(Spacing.hairline)
                            .background(palette.outline)
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s6))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.s4)
                ) {
                    FinvoraButton(
                        text = stringResource(Res.string.auth_google),
                        onClick = { viewModel.googleSignIn() },
                        style = ButtonStyle.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                    FinvoraButton(
                        text = stringResource(Res.string.auth_apple),
                        onClick = {},
                        style = ButtonStyle.SECONDARY,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(Spacing.s6))
                Row(
                    modifier = Modifier.padding(vertical = Spacing.s1),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.auth_dont_have_account_prefix),
                        style = BodyNormal(),
                        color = palette.textSecondary
                    )
                    Text(
                        text = stringResource(Res.string.auth_sign_up_link),
                        style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
                        color = palette.primary,
                        modifier = Modifier.clickable {
                            validationError = null
                            onNavigateToSignUp()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s8))
            }
        }
    }
}
