package com.finvoraai.personalfinancemanager.finvora.core.debug

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages show/hide state of the debug overlay and tracks logo tap count.
 *
 * Trigger: tap Finvora logo button to toggle overlay.
 */
object DebugController {

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible.asStateFlow()

    fun onLogoTapped() {
        if (!isEnabled()) return
        _isVisible.value = !_isVisible.value
    }

    fun hide() {
        _isVisible.value = false
    }

    /** Returns true only in debug builds — used to guard all debug UI rendering. */
    fun isEnabled(): Boolean = DebugBuildCheck.isDebug()
}
