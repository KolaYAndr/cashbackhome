package org.homesharing.cashbackhome.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "profile_settings")
data class ProfileSettings(
    @PrimaryKey
    val settingsId: Long = 1L,
    val themeMode: ThemeMode = ThemeMode.System,
)

enum class ThemeMode(val databaseValue: String) {
    System("system"),
    Dark("dark"),
    Light("light");

    companion object {
        fun fromDatabaseValue(value: String): ThemeMode =
            values().firstOrNull { it.databaseValue == value } ?: System
    }
}

internal class ThemeModeConverter {
    @TypeConverter
    fun fromThemeMode(themeMode: ThemeMode): String = themeMode.databaseValue

    @TypeConverter
    fun toThemeMode(value: String): ThemeMode = ThemeMode.fromDatabaseValue(value)
}
