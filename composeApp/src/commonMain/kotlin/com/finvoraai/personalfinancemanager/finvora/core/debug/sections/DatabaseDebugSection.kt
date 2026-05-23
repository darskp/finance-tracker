package com.finvoraai.personalfinancemanager.finvora.core.debug.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugColors
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugSectionCard
import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugStaticRow
import com.finvoraai.personalfinancemanager.finvora.data.local.AppDatabase

/**
 * Live Room DB section — directly observes SettingsDao flows.
 *
 * Unlike other sections, this does NOT use DebugLog.log().
 * It observes the DAO directly so it always reflects the live DB state,
 * even if no ViewModel has logged a change.
 *
 * Injected AppDatabase comes from Koin via DebugOverlay → koinInject<AppDatabase>().
 *
 */
@Composable
fun DatabaseDebugSection(db: AppDatabase) {
    val themeSetting by db.getSettingsDao().getThemeSetting().collectAsState(initial = null)
    val authSetting by db.getSettingsDao().getAuthSetting().collectAsState(initial = null)

    DebugSectionCard(
        title = "ROOM DB",
        accentColor = DebugColors.Database
    ) {
        Column {
            DebugStaticRow(
                label = "ThemeSetting.isDarkMode",
                value = themeSetting?.isDarkMode?.toString() ?: "null (no row)"
            )
            DebugStaticRow(
                label = "AuthSetting.hasOnboarded",
                value = authSetting?.hasCompletedOnboarding?.toString() ?: "null (no row)"
            )
        }
    }
}

// ─── Shared empty-state helper used by all sections ──────────────────────────

@Composable
fun EmptySection(message: String) {
    Text(
        text = message,
        color = DebugColors.TextMuted,
        fontSize = 10.sp,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}
