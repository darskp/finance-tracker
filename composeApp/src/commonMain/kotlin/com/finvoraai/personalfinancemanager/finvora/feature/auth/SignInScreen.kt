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

@Composable
fun SignInScreen(onSignInSuccess: () -> Unit = {}, onNavigateToSignUp: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

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
                    visible = error != null,
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
                            text = error ?: "",
                            color = palette.error,
                            style = BodyNormal(),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButtonComponent(
                            painter = painterResource(Res.drawable.ic_close),
                            onClick = { error = null },
                            contentDescription = "Dismiss error",
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
                        modifier = Modifier.clickable {}
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

                if (loading) {
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
                            if (!loading) {
                                error = null
                                emailError = false
                                passwordError = false

                                val emailVal = AuthValidator.validateEmail(email)
                                if (emailVal is ValidationResult.Failure) {
                                    error = emailVal.message
                                    emailError = true
                                } else {
                                    val passwordVal = AuthValidator.validatePassword(password)
                                    if (passwordVal is ValidationResult.Failure) {
                                        error = passwordVal.message
                                        passwordError = true
                                    } else {
                                        loading = true
                                        onSignInSuccess()
                                    }
                                }
                            }
                        },
                        style = ButtonStyle.PRIMARY,
                        enabled = !loading
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
                        onClick = {},
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
                            error = null
                            onNavigateToSignUp()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.s8))
            }
        }
    }
}
