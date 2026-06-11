package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.runtime.Composable
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntry
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntryRow
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard

@Composable
fun DashboardDebugSection(entries: List<DebugEntry>) {
    DebugSectionCard(
        title = "DASHBOARD",
        accentColor = DebugColors.Dashboard
    ) {
        if (entries.isEmpty()) {
            EmptySection("No dashboard data loaded yet…")
        } else {
            entries.forEach { DebugEntryRow(it) }
        }
    }
}
