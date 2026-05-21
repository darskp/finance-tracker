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
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H2TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ForgotPasswordScreen(
    onResetSuccess: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {},
    viewModel: AuthViewModel = koinViewModel()
) {
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf(false) }
    var codeError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()
    val isLoading = uiState is AuthUiState.Loading
    val codeSent = uiState is AuthUiState.PasswordResetCodeSent
    val apiError = (uiState as? AuthUiState.Error)?.message
    val displayError = validationError ?: apiError

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AuthEvent.NavigateToDashboard -> onResetSuccess()
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
                    text = if (codeSent) {
                        stringResource(Res.string.auth_forgot_code_sent_subtitle, email)
                    } else {
                        stringResource(Res.string.auth_forgot_subtitle)
                    },
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

                if (!codeSent) {
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
                            text = stringResource(Res.string.auth_forgot_send_code_btn),
                            onClick = {
                                validationError = null
                                emailError = false
                                val emailVal = AuthValidator.validateEmail(email)
                                if (emailVal is ValidationResult.Failure) {
                                    validationError = emailVal.message
                                    emailError = true
                                } else {
                                    viewModel.forgotPassword(email)
                                }
                            },
                            style = ButtonStyle.PRIMARY,
                            enabled = !isLoading
                        )
                    }
                } else {
                    AppTextField(
                        value = code,
                        onValueChange = {
                            code = it
                            codeError = false
                        },
                        label = stringResource(Res.string.auth_forgot_code_label),
                        placeholder = stringResource(Res.string.auth_forgot_code_placeholder),
                        isError = codeError
                    )

                    Spacer(modifier = Modifier.height(Spacing.s4))

                    AppTextField(
                        value = newPassword,
                        onValueChange = {
                            newPassword = it
                            passwordError = false
                        },
                        label = stringResource(Res.string.auth_forgot_new_password_label),
                        placeholder = stringResource(Res.string.auth_password_placeholder),
                        isPassword = true,
                        isError = passwordError
                    )

                    Spacer(modifier = Modifier.height(Spacing.s4))

                    AppTextField(
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            confirmPasswordError = false
                        },
                        label = stringResource(Res.string.auth_forgot_confirm_new_password_label),
                        placeholder = stringResource(Res.string.auth_confirm_password_placeholder),
                        isPassword = true,
                        isError = confirmPasswordError
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
                            text = stringResource(Res.string.auth_forgot_reset_btn),
                            onClick = {
                                validationError = null
                                codeError = false
                                passwordError = false
                                confirmPasswordError = false

                                val codeVal = AuthValidator.validateOtp(code)
                                if (codeVal is ValidationResult.Failure) {
                                    validationError = codeVal.message
                                    codeError = true
                                } else {
                                    val passwordVal = AuthValidator.validatePassword(newPassword)
                                    if (passwordVal is ValidationResult.Failure) {
                                        validationError = passwordVal.message
                                        passwordError = true
                                    } else {
                                        val confirmVal = AuthValidator.validateConfirmPassword(
                                            newPassword,
                                            confirmPassword
                                        )
                                        if (confirmVal is ValidationResult.Failure) {
                                            validationError = confirmVal.message
                                            confirmPasswordError = true
                                        } else {
                                            viewModel.resetPassword(code, newPassword)
                                        }
                                    }
                                }
                            },
                            style = ButtonStyle.PRIMARY,
                            enabled = !isLoading
                        )
                    }
                }

                Spacer(modifier = Modifier.height(Spacing.s6))

                Text(
                    text = stringResource(Res.string.auth_forgot_back_to_sign_in),
                    style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
                    color = palette.primary,
                    modifier = Modifier.clickable {
                        validationError = null
                        viewModel.resetState()
                        onNavigateToSignIn()
                    }
                )

                Spacer(modifier = Modifier.height(Spacing.s8))
            }
        }
    }
}
