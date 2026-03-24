package com.homesharing.cashbackhome.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.homesharing.cashbackhome.data.local.database.CardCashbackDatabase

fun createCardCashbackDatabaseBuilder(context: Context): RoomDatabase.Builder<CardCashbackDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("card_cashback.db").path
    return Room.databaseBuilder<CardCashbackDatabase>(
        context = appContext,
        name = dbFile,
    )
}