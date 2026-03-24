package com.homesharing.cashbackhome.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.homesharing.cashbackhome.data.local.database.entity.BankCard
import com.homesharing.cashbackhome.data.local.database.entity.CardCashback
import com.homesharing.cashbackhome.data.local.database.entity.CashbackCategoryConverter
import com.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [BankCard::class, CashbackRule::class, CardCashback::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(value = [CashbackCategoryConverter::class])
@ConstructedBy(CardCashbackDatabaseConstructor::class)
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

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

expect object CardCashbackDatabaseConstructor : RoomDatabaseConstructor<CardCashbackDatabase> {
    override fun initialize(): CardCashbackDatabase
}