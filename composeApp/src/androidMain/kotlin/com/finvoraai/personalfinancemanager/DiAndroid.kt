package com.finvoraai.personalfinancemanager

import android.app.Application
import com.finvoraai.personalfinancemanager.finvora.ui.analytics.FirebaseAnalyticsAndroid
import com.finvoraai.personalfinancemanager.finvora.ui.utils.ShareHelper
import createDataStore
import getDatabaseBuilder
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

val androidModule = module {
    single { getDatabaseBuilder(androidContext()) }
    single { createDataStore(androidContext()) }
    single<ShareHelper> { AndroidShareHelper(androidContext()) }
    single { FirebaseAnalyticsAndroid() }
    single {
        val jsonSerializer = get<Json>()
        io.ktor.client.HttpClient(io.ktor.client.engine.okhttp.OkHttp) {
            engine {
                config {
                    cache(
                        okhttp3.Cache(
                            directory = androidContext().cacheDir.resolve("http_cache"),
                            // 20 MB
                            maxSize = 20L * 1024 * 1024
                        )
                    )
                }
            }
            install(ContentNegotiation) {
                json(jsonSerializer)
            }
            install(Logging) {
                level = LogLevel.NONE
            }
        }
    }
}

actual fun platformModule(): Module = androidModule

fun initKoin(application: Application) {
    initKoin { koinApplication ->
        koinApplication.apply {
            androidContext(application)
        }
    }
}
