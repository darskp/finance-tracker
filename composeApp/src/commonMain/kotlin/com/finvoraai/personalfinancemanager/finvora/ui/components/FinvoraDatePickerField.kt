package com.finvoraai.personalfinancemanager.finvora.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyLarge
import com.finvoraai.personalfinancemanager.finvora.ui.theme.BodyNormal
import com.finvoraai.personalfinancemanager.finvora.ui.theme.LocalAppPalette
import com.finvoraai.personalfinancemanager.finvora.ui.theme.Spacing
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.ic_date_range
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource

// ---------------------------------------------------------------------------
// FinvoraDatePickerField
//
// A tap-to-open date field that looks identical to AppTextField but is backed
// by a real Material 3 DatePickerDialog (via FinvoraDatePickerDialog).
//
// Drop this anywhere you need a date input — it manages its own dialog state
// internally so the caller only receives the confirmed LocalDate.
//
// API
// ───
//   selectedDate   — currently selected date (null → placeholder is shown)
//   onDateSelected — delivers the confirmed LocalDate to the caller
//   label          — optional field label shown above the row
//   placeholder    — text shown when selectedDate is null
//   minDate        — optional lower bound forwarded to the dialog
//   maxDate        — optional upper bound forwarded to the dialog
//   modifier       — standard Compose modifier
//   dateFormatter  — controls how LocalDate is displayed (default: "dd MMM yyyy")
// ---------------------------------------------------------------------------

@Composable
fun FinvoraDatePickerField(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Select date",
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    dateFormatter: (LocalDate) -> String = { date ->
        val day = date.dayOfMonth.toString().padStart(2, '0')
        val month = date.month.name.take(3)
            .lowercase()
            .replaceFirstChar { it.uppercase() }
        val year = date.year
        "$day $month $year"
    }
) {
    val palette = LocalAppPalette.current
    var showDialog by remember { mutableStateOf(false) }

    // Show the dialog when the field is tapped.
    if (showDialog) {
        FinvoraDatePickerDialog(
            initialDate = selectedDate,
            onDateSelected = { date ->
                onDateSelected(date)
                showDialog = false
            },
            onDismiss = { showDialog = false },
            minDate = minDate,
            maxDate = maxDate
        )
    }

    Column(modifier = modifier) {
        // ── Optional label ────────────────────────────────────────────────
        if (label != null) {
            Text(
                text = label,
                style = BodyNormal().copy(fontWeight = FontWeight.Medium),
                color = palette.textPrimary.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = Spacing.s2)
            )
        }

        // ── Tap target — visually matches AppTextField ────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Spacing.s13)
                .clip(RoundedCornerShape(Spacing.s3))
                .background(palette.surface)
                .border(
                    width = Spacing.hairline,
                    color = palette.outline,
                    shape = RoundedCornerShape(Spacing.s3)
                )
                .clickable { showDialog = true },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.s3Half),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Date text or placeholder
                Text(
                    text = selectedDate?.let(dateFormatter) ?: placeholder,
                    style = BodyLarge().copy(fontWeight = FontWeight.Normal),
                    color = if (selectedDate != null) {
                        palette.textPrimary
                    } else {
                        palette.textSecondary.copy(alpha = 0.5f)
                    },
                    modifier = Modifier.weight(1f)
                )

                // Calendar icon trailing
                Icon(
                    painter = painterResource(Res.drawable.ic_date_range),
                    contentDescription = "Pick date",
                    tint = palette.textSecondary.copy(alpha = 0.6f),
                    modifier = Modifier.size(Spacing.s5)
                )
            }
        }
    }
}
