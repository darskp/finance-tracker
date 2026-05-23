package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.runtime.Composable
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntry
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugEntryRow
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard

/**
 * Renders Onboarding section entries logged via:
 *   DebugLog.log("Onboarding", "hasCompletedOnboarding", ...)
 *
 * Data is pushed from OnBoardingViewModel using DebugLog.log().
 *
 */
@Composable
fun OnboardingDebugSection(entries: List<DebugEntry>) {
    DebugSectionCard(
        title = "ONBOARDING",
        accentColor = DebugColors.Onboarding
    ) {
        if (entries.isEmpty()) {
            EmptySection("Waiting for onboarding events…")
        } else {
            entries.forEach { DebugEntryRow(it) }
        }
    }
}
