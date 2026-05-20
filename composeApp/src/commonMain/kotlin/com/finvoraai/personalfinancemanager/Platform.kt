package com.finvoraai.personalfinancemanager

interface Platform {
    val name: String
    val appVersion: String
    val deviceInfo: String
}

expect fun getPlatform(): Platform
