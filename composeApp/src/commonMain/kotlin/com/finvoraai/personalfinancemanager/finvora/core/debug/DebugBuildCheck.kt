package com.finvoraai.personalfinancemanager.finvora.core.debug

/**
 * Platform-specific build check + time label.
 *
*/
expect object DebugBuildCheck {
    fun isDebug(): Boolean
    fun currentTimeLabel(): String
}
