package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.runtime.Composable
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntry
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntryRow
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard

/**
 * Renders Auth section entries logged via:
 *   DebugLog.log("Auth", "authState", ...)
 *   DebugLog.log("Auth", "uiState", ...)
 *   DebugLog.log("Auth", "userId", ...)
 *   DebugLog.log("Auth", "userEmail", ...)
 *
 * Data is pushed from AuthViewModel using DebugLog.log().
 *
 */
@Composable
fun AuthDebugSection(entries: List<DebugEntry>) {
    DebugSectionCard(
        title = "AUTH",
        accentColor = DebugColors.Auth
    ) {
        if (entries.isEmpty()) {
            EmptySection("Waiting for auth events…")
        } else {
            entries.forEach { DebugEntryRow(it) }
        }
    }
}
