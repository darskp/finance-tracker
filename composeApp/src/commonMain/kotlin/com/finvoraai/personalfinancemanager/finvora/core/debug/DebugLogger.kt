package com.finvoraai.personalfinancemanager.finvora.core.debug

/**
 * Single entry point for all structured debug logging.
 *
 * This relies on a Zero-Touch Compiler-Flag Architecture.
 * When DebugBuildCheck.isDebug() is false (Production), ProGuard/R8
 * will safely eliminate these calls from the final binary.
 */
object DebugLogger {

    @PublishedApi
    internal fun logInternal(section: String, tag: String, value: Any?) {
        if (!DebugBuildCheck.isDebug()) return
        val entry = DebugEntry(
            section = section,
            tag = tag,
            value = value?.toString() ?: "null",
            timeLabel = DebugBuildCheck.currentTimeLabel()
        )
        DebugStateManager.push(entry)
        println("[FinvoraDebug][$section] $tag = $value")
    }

    inline fun auth(tag: String, value: Any?) = logInternal("Auth", tag, value)
    inline fun onboarding(tag: String, value: Any?) = logInternal("Onboarding", tag, value)
    inline fun navigation(tag: String, value: Any?) = logInternal("Navigation", tag, value)
    inline fun network(tag: String, value: Any?) = logInternal("API", tag, value)
    inline fun database(tag: String, value: Any?) = logInternal("Database", tag, value)
    inline fun generic(section: String, tag: String, value: Any?) = logInternal(section, tag, value)
}
