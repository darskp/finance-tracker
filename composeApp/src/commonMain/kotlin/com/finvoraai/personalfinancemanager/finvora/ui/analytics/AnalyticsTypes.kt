package com.finvoraai.personalfinancemanager.finvora.ui.analytics

/**
 * Supported GA4 parameter types.
 * Using a sealed class ensures type safety at the common layer.
 */
sealed class ParamValue {
    data class Str(val value: String) : ParamValue()
    data class Num(val value: Long) : ParamValue()
    data class Dec(val value: Double) : ParamValue()
}

/**
 * Typealias for analytics parameters to ensure consistency.
 */
typealias AnalyticsParams = Map<String, ParamValue>

/**
 * Centralized keys for all analytics parameters to avoid typos.
 */
object ParamKeys {
    // Infrastructure
    const val SCREEN = "screen"
    const val SOURCE = "source"
    const val TIME = "time"

    // Finvora AI Specific
    // Future Work: Add Finvora AI Param Keys (e.g., AI_MODEL_VERSION, TRANSACTION_TYPE)
    const val FEATURE_ID = "feature_id"
    const val FEATURE_NAME = "feature_name"

    // Interaction Specific
    const val INDEX = "index"
    const val SWIPE_COUNT = "swipe_count"
    const val QUERY = "query"
    const val SUB_CATEGORY_ID = "sub_category_id"
    const val PAGE_NUMBER = "page_number"
    const val STEP_INDEX = "step_index"
    const val TITLE = "title"
    const val ITEM_COUNT = "item_count"
}

/**
 * Enforces GA4 limits to prevent silent data drops.
 * Limit: Key <= 40 chars, Value <= 100 chars.
 */
object Ga4Sanitizer {
    private const val MAX_KEY_LENGTH = 40
    private const val MAX_VALUE_LENGTH = 100

    fun sanitize(params: AnalyticsParams): AnalyticsParams {
        return params.mapKeys { (key, _) ->
            key.take(MAX_KEY_LENGTH)
        }.mapValues { (_, value) ->
            when (value) {
                is ParamValue.Str -> ParamValue.Str(value.value.take(MAX_VALUE_LENGTH))
                else -> value
            }
        }
    }
}
