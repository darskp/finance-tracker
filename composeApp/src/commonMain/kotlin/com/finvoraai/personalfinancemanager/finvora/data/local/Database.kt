package com.finvoraai.personalfinancemanager.finvora.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.finvoraai.personalfinancemanager.finvora.data.model.AuthSetting
import com.finvoraai.personalfinancemanager.finvora.data.model.ThemeSetting

@Database(entities = [ThemeSetting::class, AuthSetting::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getSettingsDao(): SettingsDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
