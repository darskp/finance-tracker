package com.finvoraai.personalfinancemanager

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val appVersion: String = "${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    override val deviceInfo: String = "Android ${Build.VERSION.RELEASE} | ${Build.MODEL} | API ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
