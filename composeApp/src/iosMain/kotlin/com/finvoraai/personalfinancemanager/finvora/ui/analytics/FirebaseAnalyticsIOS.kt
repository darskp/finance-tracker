package com.finvoraai.personalfinancemanager.finvora.ui.analytics

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.analytics.analytics

class FirebaseAnalyticsIOS : AnalyticsProvider {
    private val analytics by lazy { Firebase.analytics }

    override fun logEvent(name: String, params: Map<String, Any>) {
        analytics.logEvent(name, params)
    }
}
