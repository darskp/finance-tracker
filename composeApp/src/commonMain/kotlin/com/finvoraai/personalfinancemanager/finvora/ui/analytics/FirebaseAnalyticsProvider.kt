package com.finvoraai.personalfinancemanager.finvora.ui.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

/**
 * Production provider for Firebase Analytics.
 * Automatically handles KMP dispatching to Android/iOS native SDKs.
 */
class FirebaseAnalyticsProvider : AnalyticsProvider {

    private val firebaseAnalytics = Firebase.analytics

    override fun logEvent(name: String, params: Map<String, Any>) {
        // Convert Map<String, Any> to Map<String, Any?> and ensure types are GA4 compatible
        val firebaseParams = params.mapValues { (_, value) ->
            when (value) {
                is Int -> value.toLong()
                is Float -> value.toDouble()
                else -> value
            }
        }
        firebaseAnalytics.logEvent(name, firebaseParams)
    }
}
