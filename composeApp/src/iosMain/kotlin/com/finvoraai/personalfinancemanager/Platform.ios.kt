package com.finvoraai.personalfinancemanager

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val appVersion: String = NSBundle.mainBundle.infoDictionary?.get(
        "CFBundleShortVersionString"
    ) as? String ?: "1.0"
    override val deviceInfo: String = "${UIDevice.currentDevice.model} | iOS ${UIDevice.currentDevice.systemVersion}"
}

actual fun getPlatform(): Platform = IOSPlatform()
