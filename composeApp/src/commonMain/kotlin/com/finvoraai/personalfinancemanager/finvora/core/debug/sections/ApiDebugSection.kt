package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.runtime.Composable
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntry
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntryRow
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard

/**
 * Renders API section entries logged via:
 *   DebugLog.log("API", "POST /endpoint", response.toString())
 *   DebugLog.log("API", "GET /user/profile", responseBody)
 *
 * Call DebugLog.log("API", ...) from any Repository or ViewModel after receiving an API response.
 * All values shown are real runtime responses — nothing is hardcoded.
 *
 */
@Composable
fun ApiDebugSection(entries: List<DebugEntry>) {
    DebugSectionCard(
        title = "API",
        accentColor = DebugColors.Api
    ) {
        if (entries.isEmpty()) {
            EmptySection("No API calls logged yet…")
        } else {
            entries.forEach { DebugEntryRow(it) }
        }
    }
}
