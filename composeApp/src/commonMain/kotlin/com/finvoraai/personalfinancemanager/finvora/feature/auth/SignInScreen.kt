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

private const val OTP_MIN_LENGTH = 6

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

    val isClientTrustMode = uiState is AuthUiState.ClientTrustCodeSent
    val clientTrustError = (uiState as? AuthUiState.ClientTrustCodeSent)?.error
    var clientTrustCode by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.NavigateToDashboard -> onSignInSuccess()
                else -> {}
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
                    modifier = Modifier.size(Spacing.s16),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.finvoraai_logo_no_text),
                        contentDescription = stringResource(Res.string.cd_logo),
                        modifier = Modifier.size(Spacing.s12)
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s1))

                if (isClientTrustMode) {
                    ClientTrustSection(
                        isLoading = isLoading,
                        clientTrustCode = clientTrustCode,
                        clientTrustError = clientTrustError,
                        onCodeChange = { clientTrustCode = it },
                        onVerify = { viewModel.verifyClientTrustCode(clientTrustCode) },
                        onDismissError = { viewModel.resetState() },
                        onBack = {
                            clientTrustCode = ""
                            viewModel.resetState()
                        }
                    )
                } else {
                    SignInFormSection(
                        email = email,
                        password = password,
                        isLoading = isLoading,
                        displayError = displayError,
                        emailError = emailError,
                        passwordError = passwordError,
                        onEmailChange = {
                            email = it
                            emailError = false
                        },
                        onPasswordChange = {
                            password = it
                            passwordError = false
                        },
                        onSignIn = {
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
                        onGoogleSignIn = { viewModel.googleSignIn() },
                        onDismissError = {
                            validationError = null
                            viewModel.resetState()
                        },
                        onForgotPassword = {
                            validationError = null
                            onNavigateToForgotPassword()
                        },
                        onNavigateToSignUp = {
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

// ─── Client Trust (OTP) section ───────────────────────────────────────────────

@Composable
private fun ClientTrustSection(
    isLoading: Boolean,
    clientTrustCode: String,
    clientTrustError: String?,
    onCodeChange: (String) -> Unit,
    onVerify: () -> Unit,
    onDismissError: () -> Unit,
    onBack: () -> Unit
) {
    val palette = LocalAppPalette.current

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

    Text(
        text = stringResource(Res.string.auth_verify_email_title),
        style = H2TextStyle(),
        color = palette.primary,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(Spacing.s2))

    Text(
        text = stringResource(Res.string.auth_client_trust_subtitle),
        style = BodyLarge(),
        color = palette.textSecondary,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(Spacing.s6))

    if (clientTrustError != null) {
        SignInErrorBanner(
            message = clientTrustError,
            onDismiss = onDismissError
        )
    }

    AppTextField(
        value = clientTrustCode,
        onValueChange = onCodeChange,
        label = stringResource(Res.string.auth_verification_code_label),
        placeholder = stringResource(Res.string.auth_verification_code_placeholder),
        isError = clientTrustError != null
    )

    Spacer(modifier = Modifier.height(Spacing.s6))

    if (isLoading) {
        SignInLoadingIndicator()
    } else {
        FinvoraButton(
            text = stringResource(Res.string.auth_verify_complete_btn),
            onClick = { if (clientTrustCode.length >= OTP_MIN_LENGTH) onVerify() },
            style = ButtonStyle.PRIMARY,
            enabled = clientTrustCode.length >= OTP_MIN_LENGTH
        )
    }

    Spacer(modifier = Modifier.height(Spacing.s4))

    Text(
        text = stringResource(Res.string.auth_back_to_sign_in),
        style = BodyNormal(),
        color = palette.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.clickable { onBack() }
    )
}

// ─── Normal sign-in form section ──────────────────────────────────────────────

@Suppress("LongParameterList")
@Composable
private fun SignInFormSection(
    email: String,
    password: String,
    isLoading: Boolean,
    displayError: String?,
    emailError: Boolean,
    passwordError: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignIn: () -> Unit,
    onGoogleSignIn: () -> Unit,
    onDismissError: () -> Unit,
    onForgotPassword: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val palette = LocalAppPalette.current

    AnimatedVisibility(
        visible = displayError != null,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        SignInErrorBanner(
            message = displayError ?: "",
            onDismiss = onDismissError
        )
    }

    AppTextField(
        value = email,
        onValueChange = onEmailChange,
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
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onForgotPassword() }
        )
    }

    Spacer(modifier = Modifier.height(Spacing.s2))

    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = null,
        placeholder = stringResource(Res.string.auth_password_placeholder),
        isPassword = true,
        leadingIconPainter = painterResource(Res.drawable.ic_lock),
        isError = passwordError
    )

    Spacer(modifier = Modifier.height(Spacing.s6))

    if (isLoading) {
        SignInLoadingIndicator()
    } else {
        FinvoraButton(
            text = stringResource(Res.string.auth_sign_in_btn),
            onClick = onSignIn,
            style = ButtonStyle.PRIMARY,
            enabled = !isLoading
        )
    }

    Spacer(modifier = Modifier.height(Spacing.s6))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f).height(Spacing.hairline).background(palette.outline))
        Text(
            text = stringResource(Res.string.auth_or),
            style = BodySmall(),
            color = palette.textSecondary,
            modifier = Modifier.padding(horizontal = Spacing.s4)
        )
        Box(modifier = Modifier.weight(1f).height(Spacing.hairline).background(palette.outline))
    }

    Spacer(modifier = Modifier.height(Spacing.s6))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.s4)
    ) {
        FinvoraButton(
            text = stringResource(Res.string.auth_google),
            onClick = onGoogleSignIn,
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
            modifier = Modifier.clickable { onNavigateToSignUp() }
        )
    }
}

// ─── Shared small helpers ─────────────────────────────────────────────────────

@Composable
private fun SignInErrorBanner(message: String, onDismiss: () -> Unit) {
    val palette = LocalAppPalette.current
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
            text = message,
            color = palette.error,
            style = BodyNormal(),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        IconButtonComponent(
            painter = painterResource(Res.drawable.ic_close),
            onClick = onDismiss,
            contentDescription = stringResource(Res.string.cd_dismiss_error),
            tint = palette.error,
            modifier = Modifier.size(Spacing.s4)
        )
    }
}

@Composable
private fun SignInLoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = LocalAppPalette.current.primary,
            strokeWidth = 3.dp,
            modifier = Modifier.size(Spacing.s6)
        )
    }
}
