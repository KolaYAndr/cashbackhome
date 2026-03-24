package org.homesharing.cashbackhome.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.homesharing.cashbackhome.data.local.database.CardCashbackDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun createCardCashbackDatabaseBuilder(): RoomDatabase.Builder<CardCashbackDatabase> {
    val directory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    val basePath = requireNotNull(directory?.path)
    return Room.databaseBuilder<CardCashbackDatabase>(
        name = "$basePath/database.db"
    )
}