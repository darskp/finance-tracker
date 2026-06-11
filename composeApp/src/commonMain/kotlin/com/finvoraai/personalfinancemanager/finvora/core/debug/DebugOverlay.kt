@file:Suppress("ForbiddenImport")

package com.finvoraai.personalfinancemanager.finvora.core.debug

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.ApiDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.AuthDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.DashboardDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.DatabaseDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.NavigationDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.debug.sections.OnboardingDebugSection
import com.finvoraai.personalfinancemanager.finvora.core.network.BASE_API_URL
import com.finvoraai.personalfinancemanager.finvora.data.local.AppDatabase
import finvoraai.composeapp.generated.resources.Res
import finvoraai.composeapp.generated.resources.cd_debug_close
import finvoraai.composeapp.generated.resources.debug_clear_all
import finvoraai.composeapp.generated.resources.debug_copy_all
import finvoraai.composeapp.generated.resources.ic_close
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

// ─── Design tokens ────────────────────────────────────────────────────────────
@Suppress("MagicNumber")
private val BgPanel = Color(0xFF0D1117)

@Suppress("MagicNumber")
private val BgHeader = Color(0xFF161B27)

@Suppress("MagicNumber")
private val AccentGreen = Color(0xFF00E676)

@Suppress("MagicNumber")
private val TextPrimary = Color(0xFFE6EDF3)

@Suppress("MagicNumber")
private val TextMuted = Color(0xFF8B949E)

@Suppress("MagicNumber")
private val DividerColor = Color(0xFF21262D)

// ─── Animation / layout constants ────────────────────────────────────────────
private const val ANIM_BACKDROP_IN = 300
private const val ANIM_BACKDROP_OUT = 250
private const val ANIM_PANEL_IN = 320
private const val ANIM_PANEL_OUT = 260

@Suppress("MagicNumber")
private val PanelHeightFraction = 0.68f
private const val SECTION_CORNER_DP = 10
private const val ENTRY_VALUE_MAX_CHARS = 120

/**
 * Root debug overlay panel — slides up from the bottom covering 65% of the screen.
 *
 * Renders all registered debug sections inside a scrollable LazyColumn.
 * Tapping the dimmed area behind the panel closes it.
 *
 */
@Composable
fun DebugOverlay() {
    if (!DebugController.isEnabled()) return

    val isVisible by DebugController.isVisible.collectAsState()
    val sections by DebugStateManager.sections.collectAsState()
    val db: AppDatabase = koinInject()

    Box(modifier = Modifier.fillMaxSize()) {
        // ── Dim backdrop ──────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(tween(ANIM_BACKDROP_IN)) { it },
            exit = slideOutVertically(tween(ANIM_BACKDROP_OUT)) { it }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.55f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { DebugController.hide() }
            )
        }

        // ── Panel ─────────────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = isVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(tween(ANIM_PANEL_IN)) { it },
            exit = slideOutVertically(tween(ANIM_PANEL_OUT)) { it }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(PanelHeightFraction)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(BgPanel)
            ) {
                DebugPanelHeader(
                    sections = sections,
                    onClose = { DebugController.hide() },
                    onClearAll = { DebugStateManager.clearAll() }
                )
                HorizontalDivider(color = DividerColor, thickness = 0.5.dp)

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item { Spacer(Modifier.height(8.dp)) }

                    // ── Static config info ─────────────────────────────────────
                    item {
                        DebugSectionCard(title = "CONFIG", accentColor = AccentGreen) {
                            DebugStaticRow("API Base URL", BASE_API_URL)
                        }
                    }

                    // ── Named sections (populated by DebugLog.log calls) ──────
                    item { AuthDebugSection(entries = sections["Auth"] ?: emptyList()) }
                    item { OnboardingDebugSection(entries = sections["Onboarding"] ?: emptyList()) }
                    item { NavigationDebugSection(entries = sections["Navigation"] ?: emptyList()) }
                    item { ApiDebugSection(entries = sections["API"] ?: emptyList()) }
                    item { DashboardDebugSection(entries = sections["Dashboard"] ?: emptyList()) }

                    // ── Live Room DB section (observes DAOs directly) ─────────
                    item { DatabaseDebugSection(db = db) }

                    // ── Any extra sections logged from future features ────────
                    val extraKeys = sections.keys - setOf("Auth", "Onboarding", "Navigation", "API", "Dashboard")
                    items(extraKeys.toList()) { key ->
                        GenericDebugSection(
                            title = key,
                            entries = sections[key] ?: emptyList()
                        )
                    }

                    item { Spacer(Modifier.height(24.dp)) }
                }
            }
        }
    }
}

// ─── Header bar ───────────────────────────────────────────────────────────────

@Composable
private fun DebugPanelHeader(sections: Map<String, List<DebugEntry>>, onClose: () -> Unit, onClearAll: () -> Unit) {
    val sectionCount = sections.size
    val clipboardManager = LocalClipboardManager.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BgHeader)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("🐛", fontSize = 18.sp)
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Debug",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$sectionCount section${if (sectionCount != 1) "s" else ""} active",
                color = TextMuted,
                fontSize = 10.sp
            )
        }
        TextButton(
            onClick = {
                val formattedText = buildString {
                    appendLine("=== FINVORA DEBUG REPORT ===")
                    sections.forEach { (sectionName, entries) ->
                        appendLine("\n--- $sectionName ---")
                        entries.asReversed().forEach { entry ->
                            appendLine("[${entry.timeLabel}] ${entry.tag} = ${entry.value}")
                        }
                    }
                }
                clipboardManager.setText(AnnotatedString(formattedText))
            }
        ) {
            Text(stringResource(Res.string.debug_copy_all), color = AccentGreen, fontSize = 11.sp)
        }
        Spacer(Modifier.width(4.dp))
        TextButton(onClick = onClearAll) {
            Text(stringResource(Res.string.debug_clear_all), color = TextMuted, fontSize = 11.sp)
        }
        IconButton(onClick = onClose) {
            Icon(
                painter = painterResource(Res.drawable.ic_close),
                contentDescription = stringResource(Res.string.cd_debug_close),
                tint = TextMuted
            )
        }
    }
}

// ─── Generic fallback section for any future feature ──────────────────────────

@Composable
fun GenericDebugSection(title: String, entries: List<DebugEntry>) {
    if (entries.isEmpty()) return
    DebugSectionCard(title = title, accentColor = DebugColors.Generic) {
        entries.forEach { entry ->
            DebugEntryRow(entry = entry)
        }
    }
}

// ─── Shared section card shell ────────────────────────────────────────────────

@Composable
fun DebugSectionCard(title: String, accentColor: Color, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(SECTION_CORNER_DP.dp))
            .background(BgHeader)
    ) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(horizontal = 12.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(14.dp)
                    .background(accentColor, RoundedCornerShape(2.dp))
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = title,
                color = accentColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = if (expanded) "▲" else "▼",
                color = TextMuted,
                fontSize = 9.sp
            )
        }

        if (expanded) {
            HorizontalDivider(color = DividerColor, thickness = 0.5.dp)
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
                content()
            }
        }
    }
}

// ─── Shared entry row ─────────────────────────────────────────────────────────

@Composable
fun DebugEntryRow(entry: DebugEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = entry.timeLabel,
            color = TextMuted,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(52.dp)
        )
        Text(
            text = "${entry.tag}",
            color = AccentGreen.copy(alpha = 0.8f),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = "= ${entry.value.take(ENTRY_VALUE_MAX_CHARS)}",
            color = TextPrimary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun DebugStaticRow(label: String, value: String, accent: Color = AccentGreen) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = accent.copy(alpha = 0.8f),
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.width(162.dp)
        )
        Text(
            text = "= $value",
            color = TextPrimary,
            fontSize = 10.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f)
        )
    }
}
