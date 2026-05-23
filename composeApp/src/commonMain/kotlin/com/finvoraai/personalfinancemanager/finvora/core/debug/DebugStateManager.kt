package com.finvoraai.personalfinancemanager.finvora.core.debug

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A single log entry stored in the overlay.
 */
data class DebugEntry(
    val section: String,
    val tag: String,
    val value: String,
    val timeLabel: String
)

/**
 * Central Redux-style store for all debug entries.
 *
 * Data flow:
 *   ViewModel → DebugLog.log() → DebugStateManager.push() → DebugOverlay (Compose)
 *
 */
object DebugStateManager {

    private const val MAX_ENTRIES_PER_SECTION = 50

    /** Map of sectionName → ordered list of entries (newest first) */
    private val _sections = MutableStateFlow<Map<String, List<DebugEntry>>>(emptyMap())
    val sections: StateFlow<Map<String, List<DebugEntry>>> = _sections.asStateFlow()

    fun push(entry: DebugEntry) {
        _sections.update { current ->
            val existing = current[entry.section] ?: emptyList()
            val updated = listOf(entry) + existing.take(MAX_ENTRIES_PER_SECTION - 1)
            current + (entry.section to updated)
        }
    }

    fun clearSection(section: String) {
        _sections.update { it - section }
    }

    fun clearAll() {
        _sections.value = emptyMap()
    }
}
