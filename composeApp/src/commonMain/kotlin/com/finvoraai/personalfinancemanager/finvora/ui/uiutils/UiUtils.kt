@file:Suppress("MagicNumber")
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
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.TimeZone

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



/**
 * Formats epoch-milliseconds into a 12-hour clock time string (e.g. "9:32 AM").
 * Used for chat message timestamps.
 */
fun Long.formatChatTime(): String {
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(this)
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val hour = dt.hour
    val minute = dt.minute.toString().padStart(2, '0')
    val amPm = if (hour < 12) "AM" else "PM"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    return "$hour12:$minute $amPm"
}

/**
 * Formats epoch-milliseconds into a date label for chat section separators.
 * Returns "TODAY" for the current day, otherwise e.g. "MAY 13".
 */
fun Long.formatChatDateLabel(): String {
    val instant = kotlinx.datetime.Instant.fromEpochMilliseconds(this)
    val dt = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val today = kotlinx.datetime.Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
    return if (dt.date == today.date) {
        "TODAY"
    } else {
        val month = dt.month.name.take(3)
        "$month ${dt.dayOfMonth}"
    }
}

