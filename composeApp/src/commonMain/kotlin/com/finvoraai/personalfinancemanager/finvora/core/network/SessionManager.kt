package com.finvoraai.personalfinancemanager.finvora.core.network

import com.finvoraai.personalfinancemanager.finvora.core.debug.DebugLogger

object SessionManager {
    private var onSessionExpired: (() -> Unit)? = null

    fun init(callback: () -> Unit) {
        onSessionExpired = callback
        DebugLogger.auth("SessionManager", "initialized")
    }

    suspend fun onUnauthorized() {
        DebugLogger.auth("SessionManager", "401 detected — clearing session")
        val callback = onSessionExpired
        if (callback != null) {
            callback()
        } else {
            DebugLogger.auth("SessionManager", "no callback registered — session expired event swallowed")
        }
    }
}
