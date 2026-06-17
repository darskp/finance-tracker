package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

// ---------------------------------------------------------------------------
// FinvoraDatePickerDialog
//
// A standalone Material 3 DatePickerDialog wrapper styled to the app palette.
// Use this directly when you need to trigger the dialog from a button or menu
// item rather than from the built-in FinvoraDatePickerField trigger field.
//
// API
// ───
//   initialDate   — pre-selected date; null means today is not pre-selected
//   onDateSelected — delivers the confirmed LocalDate to the caller
//   onDismiss      — called when the user cancels or taps outside
//   minDate        — optional lower bound (inclusive); null = no bound
//   maxDate        — optional upper bound (inclusive); null = no bound
//
// The dialog uses kotlinx-datetime LocalDate so the result is platform-
// agnostic and easy to format / persist.
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinvoraDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    initialDate: LocalDate? = null,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
) {
    val palette = LocalAppPalette.current

    // Convert LocalDate → epoch-millis for the M3 state initialiser.
    val initialMillis = initialDate?.toEpochMillis()
    val selectableDates = rememberFinvoraSelectableDates(minDate = minDate, maxDate = maxDate)

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis,
        selectableDates = selectableDates
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis
                        ?.millisToLocalDate()
                        ?.let(onDateSelected)
                    onDismiss()
                }
            ) {
                Text(
                    text = "OK",
                    style = BodyNormal().copy(
                        fontWeight = FontWeight.SemiBold,
                        color = palette.primary
                    )
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = BodyNormal().copy(color = palette.textSecondary)
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = palette.surface,
            titleContentColor = palette.textSecondary,
            headlineContentColor = palette.textPrimary,
            weekdayContentColor = palette.textSecondary,
            subheadContentColor = palette.textSecondary,
            navigationContentColor = palette.textPrimary,
            yearContentColor = palette.textPrimary,
            currentYearContentColor = palette.primary,
            selectedYearContentColor = palette.onPrimary,
            selectedYearContainerColor = palette.primary,
            dayContentColor = palette.textPrimary,
            selectedDayContentColor = palette.onPrimary,
            selectedDayContainerColor = palette.primary,
            todayContentColor = palette.primary,
            todayDateBorderColor = palette.primary,
            disabledDayContentColor = palette.textTertiary
        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = palette.surface,
                titleContentColor = palette.textSecondary,
                headlineContentColor = palette.textPrimary,
                weekdayContentColor = palette.textSecondary,
                subheadContentColor = palette.textSecondary,
                navigationContentColor = palette.textPrimary,
                yearContentColor = palette.textPrimary,
                currentYearContentColor = palette.primary,
                selectedYearContentColor = palette.onPrimary,
                selectedYearContainerColor = palette.primary,
                dayContentColor = palette.textPrimary,
                selectedDayContentColor = palette.onPrimary,
                selectedDayContainerColor = palette.primary,
                todayContentColor = palette.primary,
                todayDateBorderColor = palette.primary,
                disabledDayContentColor = palette.textTertiary
            )
        )
    }
}

// ---------------------------------------------------------------------------
// Helpers — internal to the component package
// ---------------------------------------------------------------------------

/**
 * Converts epoch-milliseconds (UTC) to [LocalDate] using the system timezone.
 */
internal fun Long.millisToLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

/**
 * Converts a [LocalDate] to epoch-milliseconds at midnight UTC.
 * The M3 DatePicker always stores dates at UTC midnight, so this is safe.
 */
internal fun LocalDate.toEpochMillis(): Long {
    // Build an Instant at noon UTC to avoid any DST edge-cases around midnight.
    val isoString = "${year}-${monthNumber.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}T12:00:00Z"
    return Instant.parse(isoString).toEpochMilliseconds()
}

/**
 * Builds a [androidx.compose.material3.SelectableDates] that restricts the
 * picker to [minDate]..[maxDate].  Either bound may be null (= unbounded).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun rememberFinvoraSelectableDates(
    minDate: LocalDate?,
    maxDate: LocalDate?
): androidx.compose.material3.SelectableDates {
    // Capture millis outside the lambda to avoid recomposition captures.
    val minMillis = minDate?.toEpochMillis()
    val maxMillis = maxDate?.toEpochMillis()

    return remember(minMillis, maxMillis) {
        object : androidx.compose.material3.SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                if (minMillis != null && utcTimeMillis < minMillis) return false
                if (maxMillis != null && utcTimeMillis > maxMillis) return false
                return true
            }

            override fun isSelectableYear(year: Int): Boolean {
                val minYear = minDate?.year
                val maxYear = maxDate?.year
                if (minYear != null && year < minYear) return false
                if (maxYear != null && year > maxYear) return false
                return true
            }
        }
    }
}
