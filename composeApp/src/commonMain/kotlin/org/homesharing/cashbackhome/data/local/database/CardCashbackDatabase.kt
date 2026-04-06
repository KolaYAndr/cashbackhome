package org.homesharing.cashbackhome.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCardTypeConverter
import org.homesharing.cashbackhome.data.local.database.entity.CardCashback
import org.homesharing.cashbackhome.data.local.database.entity.CashbackCategoryConverter
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

@Database(
    entities = [BankCard::class, CashbackRule::class, CardCashback::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(value = [CashbackCategoryConverter::class, BankCardTypeConverter::class])
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