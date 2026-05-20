package com.finvoraai.personalfinancemanager

import android.app.Application
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.AnalyticsManager
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.FirebaseAnalyticsProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize

class FinvoraApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)

        AnalyticsManager.addProvider(FirebaseAnalyticsProvider())

        initKoin(this)
    }
}
