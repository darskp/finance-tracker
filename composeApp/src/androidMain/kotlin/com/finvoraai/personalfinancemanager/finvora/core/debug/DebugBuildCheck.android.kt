package com.finvoraai.personalfinancemanager.finvora.core.debug

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Android actual for DebugBuildCheck.
 */
actual object DebugBuildCheck {
    actual fun isDebug(): Boolean = com.finvoraai.personalfinancemanager.BuildConfig.DEBUG

    actual fun currentTimeLabel(): String {
        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return sdf.format(Date())
    }
}
