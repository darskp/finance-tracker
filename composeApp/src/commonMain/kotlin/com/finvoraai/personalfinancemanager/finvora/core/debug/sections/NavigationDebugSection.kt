package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.runtime.Composable
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntry
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntryRow
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard

/**
 * Renders Navigation section entries logged via:
 *   DebugLog.log("Navigation", "lockedDestination", ...)
 *   DebugLog.log("Navigation", "initialRoute", ...)
 *   DebugLog.log("Navigation", "isColdStart", ...)
 *
 * Data is pushed from MainScreen.kt using DebugLog.log().
 *
 */
@Composable
fun NavigationDebugSection(entries: List<DebugEntry>) {
    DebugSectionCard(
        title = "NAVIGATION",
        accentColor = DebugColors.Navigation
    ) {
        if (entries.isEmpty()) {
            EmptySection("Waiting for navigation events…")
        } else {
            entries.forEach { DebugEntryRow(it) }
        }
    }
}
