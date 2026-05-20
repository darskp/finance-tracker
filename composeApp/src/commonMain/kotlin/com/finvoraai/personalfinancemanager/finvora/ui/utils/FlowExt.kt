package com.finvoraai.personalfinancemanager.finvora.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Extension function to collect a Flow as State with Lifecycle awareness.
 * This helps in preventing background collection waste.
 */
@Composable
fun <T> Flow<T>.collectAsStateLifecycleAware(
    initialValue: T,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    return this.collectAsStateWithLifecycle(
        initialValue = initialValue,
        context = context
    )
}

/**
 * Extension function to collect a StateFlow as State with Lifecycle awareness.
 */
@Composable
fun <T> StateFlow<T>.collectAsStateLifecycleAware(context: CoroutineContext = EmptyCoroutineContext): State<T> {
    return this.collectAsStateWithLifecycle(
        context = context
    )
}
