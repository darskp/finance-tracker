package com.finvoraai.personalfinancemanager.finvora.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finvoraai.personalfinancemanager.finvora.ui.components.ButtonStyle
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraButton
import com.finvoraai.personalfinancemanager.finvora.ui.components.FinvoraDialog
import com.finvoraai.personalfinancemanager.finvora.ui.components.IconButtonComponent
import com.finvoraai.personalfinancemanager.finvora.ui.components.OtpInputField
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.H4TextStyle
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import com.finvoraai.personalfinancemanager.finvora.ui.theme.tokens.Motion
import com.finvoraai.personalfinancemanager.finvora.ui.uiutils.maskEmail
import finvoraai.composeapp.generated.resources.*
import finvoraai.composeapp.generated.resources.Res
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val TIMER_STRING_PAD_LENGTH = 2

@Composable
fun OtpDialog(email: String, onDismissRequest: () -> Unit, onVerifySuccess: () -> Unit) {
    val maskedEmail = remember(email) { email.maskEmail() }
    var otpCode by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var otpError by remember { mutableStateOf(false) }

    var timeLeft by remember { mutableStateOf(Motion.OTP_TIMER_SECONDS) }

    val palette = LocalAppPalette.current

    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(Motion.ONE_SECOND_MILLIS)
            timeLeft--
        }
    }

    fun performVerification() {
        if (!loading) {
            error = null
            otpError = false
            val result = AuthValidator.validateOtp(otpCode)
            if (result is ValidationResult.Failure) {
                error = result.message
                otpError = true
            } else if (otpCode != "123456") {
                // Test Environment: test OTP is 123456
                error = "Invalid OTP. Use 123456 for testing."
                otpError = true
            } else {
                loading = true
                onVerifySuccess()
            }
        }
    }

    FinvoraDialog(
        onDismissRequest = onDismissRequest
    ) {
        Text(
            text = stringResource(Res.string.auth_verify_email_title),
            style = H4TextStyle(),
            color = palette.textPrimary,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(Spacing.s2))

        Text(
            text = stringResource(Res.string.auth_verify_email_subtitle, maskedEmail),
            style = BodyNormal(),
            color = palette.textSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(Spacing.s6))

        OtpInputField(
            value = otpCode,
            onValueChange = {
                otpCode = it
                otpError = false
                error = null
                if (it.length == 6) {
                    performVerification()
                }
            },
            isError = otpError,
            keyboardActions = KeyboardActions(
                onDone = {
                    performVerification()
                }
            )
        )

        Spacer(modifier = Modifier.height(Spacing.s4))

        AnimatedVisibility(
            visible = error != null,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(Spacing.s4))

        // Timer / Resend Code
        if (timeLeft > 0) {
            Text(
                text = stringResource(
                    Res.string.auth_otp_resend_timer,
                    timeLeft.toString().padStart(TIMER_STRING_PAD_LENGTH, '0')
                ),
                style = BodyNormal(),
                color = palette.textSecondary
            )
        } else {
            Text(
                text = stringResource(Res.string.auth_otp_resend_action),
                style = BodyNormal().copy(fontWeight = FontWeight.SemiBold),
                color = palette.primary,
                modifier = Modifier.clickable {
                    timeLeft = Motion.OTP_TIMER_SECONDS
                    otpCode = ""
                    error = null
                    otpError = false
                }
            )
        }

        Spacer(modifier = Modifier.height(Spacing.s6))

        if (loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.s13),
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
                text = stringResource(Res.string.auth_verify_complete_btn),
                onClick = {
                    performVerification()
                },
                style = ButtonStyle.PRIMARY,
                enabled = !loading
            )
        }
    }
}
