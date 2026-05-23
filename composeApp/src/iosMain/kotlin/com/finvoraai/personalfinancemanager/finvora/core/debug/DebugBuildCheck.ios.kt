package com.finvoraai.personalfinancemanager.finvora.core.debug

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

/**
 * iOS actual for DebugBuildCheck.
 */
actual object DebugBuildCheck {
    actual fun isDebug(): Boolean = Platform.isDebugBinary

    actual fun currentTimeLabel(): String {
        val formatter = NSDateFormatter()
        formatter.dateFormat = "hh:mm:ss a"
        formatter.locale = NSLocale.currentLocale
        return formatter.stringFromDate(NSDate())
    }
}
