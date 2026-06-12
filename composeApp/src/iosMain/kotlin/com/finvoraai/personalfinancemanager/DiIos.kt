package com.finvoraai.personalfinancemanager

import com.finvoraai.personalfinancemanager.finvora.ui.analytics.AnalyticsManager
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.FirebaseAnalyticsIOS
import com.finvoraai.personalfinancemanager.finvora.ui.utils.ShareHelper
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.*

val iosModule = module {
    single { getDatabaseBuilder() }
    single(named("search_index_path")) {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            NSDocumentDirectory,
            NSUserDomainMask,
            null,
            false,
            null
        )
        (documentDirectory?.path + "/tagindex.bin").toPath()
    }
    single { createDataStore() }
    single<ShareHelper> { IosShareHelper() }
    single { FirebaseAnalyticsIOS() }
}

actual fun platformModule(): Module = iosModule

fun doInitKoin() {
    initKoin()
    // Initialize Analytics
    AnalyticsManager.addProvider(getKoin().get<FirebaseAnalyticsIOS>())
}

fun initFirebase() {
    dev.gitlive.firebase.Firebase.initialize()
}
