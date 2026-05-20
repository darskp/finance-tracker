package com.finvoraai.personalfinancemanager

import com.finvoraai.personalfinancemanager.finvora.ui.analytics.AnalyticsManager
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.FirebaseAnalyticsIOS
import com.finvoraai.personalfinancemanager.finvora.ui.utils.ShareHelper
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
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
    single {
        val jsonSerializer = get<Json>()
        io.ktor.client.HttpClient(io.ktor.client.engine.darwin.Darwin) {
            install(ContentNegotiation) {
                json(jsonSerializer)
            }
            install(Logging) {
                level = LogLevel.NONE
            }
        }
    }
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
