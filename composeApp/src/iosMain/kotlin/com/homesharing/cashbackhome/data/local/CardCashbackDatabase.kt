package com.homesharing.cashbackhome.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

fun createCardCashbackDatabaseBuilder(): RoomDatabase.Builder<CardCashbackDatabase> {
    val paths = NSSearchPathForDirectoriesInDomains(NSApplicationSupportDirectory, NSUserDomainMask, true)
    val basePath = paths.firstOrNull() ?: error("Cannot access application support directory")
    return Room.databaseBuilder<CardCashbackDatabase>(
        name = "$basePath/database.db"
    )
}