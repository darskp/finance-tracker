package com.finvoraai.personalfinancemanager.finvora.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.finvoraai.personalfinancemanager.finvora.data.model.AuthSetting
import com.finvoraai.personalfinancemanager.finvora.data.model.ThemeSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    // Get the setting as a Flow, so the UI can react to changes automatically
    @Query("SELECT * FROM settings WHERE id = 1")
    fun getThemeSetting(): Flow<ThemeSetting?>

    // Use @Upsert to either insert the setting or update it if it exists
    @Upsert
    suspend fun saveThemeSetting(setting: ThemeSetting)

    @Query("SELECT * FROM auth_settings WHERE id = 1")
    fun getAuthSetting(): Flow<AuthSetting?>

    @Upsert
    suspend fun saveAuthSetting(setting: AuthSetting)
}
