package com.finvoraai.personalfinancemanager

import androidx.room.RoomDatabase
import com.finvoraai.personalfinancemanager.finvora.data.local.AppDatabase
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthManager
import com.finvoraai.personalfinancemanager.finvora.feature.auth.AuthViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.chat.ChatViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.home.HomeScreenViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.main.MainViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.onboarding.OnBoardingViewModel
import com.finvoraai.personalfinancemanager.finvora.feature.welcome.WelcomeViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

private var koinRef: Koin? = null

fun initKoin(config: (KoinApplication) -> Unit = {}) {
    if (koinRef == null) {
        val app = startKoin {
            config(this)
            modules(appModule, platformModule())
        }
        koinRef = app.koin
    }
}

val appModule = module {
    single<CoroutineDispatcher> { Dispatchers.Default }
    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            allowComments = true
            isLenient = true
        }
    }

    single { get<RoomDatabase.Builder<AppDatabase>>().build() }
    single { get<AppDatabase>().getSettingsDao() }
    single { AuthManager() }

    single { com.finvoraai.personalfinancemanager.finvora.core.network.createHttpClient(get()) }
    single { com.finvoraai.personalfinancemanager.finvora.data.remote.DashboardApiService(get(), get(), get()) }
    single<com.finvoraai.personalfinancemanager.finvora.data.repository.DashboardRepository> {
        com.finvoraai.personalfinancemanager.finvora.data.repository.DashboardRepositoryImpl(get())
    }

    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::HomeScreenViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::AuthViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::WelcomeViewModel)
}

fun resetKoin() {
    stopKoin()
    koinRef = null
    initKoin()
}

expect fun platformModule(): Module

fun getKoin(): Koin = koinRef ?: throw Throwable("Koin is not initialized. Call initKoin() first.")
