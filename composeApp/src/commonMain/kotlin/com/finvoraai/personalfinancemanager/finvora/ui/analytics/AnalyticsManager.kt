package com.finvoraai.personalfinancemanager.finvora.ui.analytics

object AnalyticsManager {
    private val providers = mutableListOf<AnalyticsProvider>()

    fun addProvider(provider: AnalyticsProvider) {
        providers.add(provider)
    }

    fun log(event: AnalyticsEvent) {
        val params = event.toParams()
        println("Analytics: Logging event: ${event.name} with params: $params")
        providers.forEach { it.logEvent(event.name, params) }
    }
}

interface AnalyticsProvider {
    fun logEvent(name: String, params: Map<String, Any>)
}
