package com.finvoraai.personalfinancemanager

import android.app.Application
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.FirebaseAnalyticsAndroid
import com.finvoraai.personalfinancemanager.finvora.ui.utils.ShareHelper
import createDataStore
import getDatabaseBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {
    single { getDatabaseBuilder(androidContext()) }
    single { createDataStore(androidContext()) }
    single<ShareHelper> { AndroidShareHelper(androidContext()) }
    single { FirebaseAnalyticsAndroid() }
}

actual fun platformModule(): Module = androidModule

fun initKoin(application: Application) {
    initKoin { koinApplication ->
        koinApplication.apply {
            androidContext(application)
        }
    }
}
