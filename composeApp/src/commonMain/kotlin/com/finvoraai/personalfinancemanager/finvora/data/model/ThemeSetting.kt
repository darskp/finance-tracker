package com.finvoraai.personalfinancemanager.finvora.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class ThemeSetting(
    @PrimaryKey val id: Int = 1,
    val isDarkMode: Boolean
)
