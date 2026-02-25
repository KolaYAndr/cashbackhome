package com.homesharing.cashbackhome.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.homesharing.cashbackhome.domain.models.BankCard
import com.homesharing.cashbackhome.domain.models.CardCashback
import com.homesharing.cashbackhome.domain.models.CashbackCategoryConverter
import com.homesharing.cashbackhome.domain.models.CashbackRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [BankCard::class, CashbackRule::class, CardCashback::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(value = [CashbackCategoryConverter::class])
abstract class CardCashbackDatabase : RoomDatabase() {
    abstract fun cardCashbackDao(): CardCashbackDao
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<CardCashbackDatabase>,
): CardCashbackDatabase {
    return builder
        .addMigrations()
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}