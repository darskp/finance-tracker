package com.finvoraai.personalfinancemanager.finvora.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_settings")
data class AuthSetting(
    @PrimaryKey val id: Int = 1,
    val hasCompletedOnboarding: Boolean = false
)
