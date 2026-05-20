package com.finvoraai.personalfinancemanager.finvora.ui.analytics

/**
 * Business-focused analytics taxonomy for Finvora AI.
 * Defined core signals that drive business decisions + Screen Views.
 */
sealed class AnalyticsEvent(val name: String) {

    abstract fun toParams(): Map<String, Any>

    /**
     * Finvora AI related events will be added here.
     * These will include AI chat interactions, financial analysis triggers, and more.
     */
    // Future Work: Add Finvora AI Events (e.g., AI_QUERY, INSIGHT_GENERATED)

    /** EXAMPLE: How to add a new event with parameters */
    class SelectFeature(val featureId: String, val featureName: String) :
        AnalyticsEvent("select_feature") {
        override fun toParams() = mapOf(
            "feature_id" to featureId,
            "feature_name" to featureName
        )
    }

    /** 8. Manual Screen View */
    class ScreenView(val screenName: String, val screenClass: String) :
        AnalyticsEvent("screen_view") {
        override fun toParams() = mapOf(
            "firebase_screen" to screenName,
            "firebase_screen_class" to screenClass
        )
    }

    /** 9. Network/System Error Tracking */
    class AppError(val type: String, val message: String) :
        AnalyticsEvent("app_error") {
        override fun toParams() = mapOf(
            "error_type" to type,
            "error_message" to message
        )
    }

    /** 10. Resilience Signal */
    class RetryClick(val context: String) :
        AnalyticsEvent("retry_click") {
        override fun toParams() = mapOf(
            "retry_context" to context
        )
    }
}
