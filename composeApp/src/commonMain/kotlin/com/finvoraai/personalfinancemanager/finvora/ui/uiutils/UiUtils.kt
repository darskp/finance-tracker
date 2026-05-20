package com.finvoraai.personalfinancemanager.finvora.ui.uiutils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month

private const val SHORT_NAME_LENGTH = 3
private const val EMAIL_VISIBLE_CHARS = 3

@Composable
fun HSpacer(width: Dp = Spacing.s4) {
    Spacer(modifier = Modifier.width(width))
}

@Composable
fun VSpacer(factor: Int) {
    Spacer(modifier = Modifier.height(Spacing.s4 * factor))
}

@Composable
fun VSpacer(height: Dp) {
    Spacer(modifier = Modifier.height(height))
}

fun DayOfWeek.shortName(): String {
    return this.name.lowercase().let { lower ->
        lower.take(1).uppercase() + lower.drop(1)
    }.run {
        take(SHORT_NAME_LENGTH)
    }
}

fun Month.shortName(): String {
    return this.name.lowercase().let {
        it.take(1).uppercase() + it.drop(1)
    }.run {
        take(SHORT_NAME_LENGTH)
    }
}

fun String.maskEmail(): String {
    val atIndex = indexOf('@')
    if (atIndex <= 0) return this
    val localPart = substring(0, atIndex)
    val domain = substring(atIndex)
    val firstPart = if (localPart.length >= EMAIL_VISIBLE_CHARS) localPart.take(EMAIL_VISIBLE_CHARS) else localPart
    val lastPart = if (localPart.length >= EMAIL_VISIBLE_CHARS) localPart.takeLast(EMAIL_VISIBLE_CHARS) else localPart
    return "$firstPart******$lastPart$domain"
}
