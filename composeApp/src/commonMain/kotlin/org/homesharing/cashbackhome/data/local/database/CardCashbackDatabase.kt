package org.homesharing.cashbackhome.data.local.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
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
    version = 4,
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
        .addMigrations(Migration1To2, Migration2To3, Migration3to4)
        .fallbackToDestructiveMigrationOnDowngrade(true)
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}

private val Migration1To2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSql("ALTER TABLE cashback_rules ADD COLUMN startDate TEXT NOT NULL DEFAULT ''")
        connection.execSql(
            """
            UPDATE cashback_rules
            SET startDate =
                CASE
                    WHEN expirationDate IS NULL OR expirationDate = ' ' THEN ' '
        
                    -- Если дата не существующая при вычитании 1го месяца
                    WHEN CAST(strftime('%d', expirationDate) AS INTEGER) >
                         CAST(strftime('%d', date(expirationDate, 'start of month', '-1 day')) AS INTEGER)
                    THEN date(expirationDate, 'start of month', '-1 day')
        
                    -- Если дата сущетствует при вычитании 1го месяца
                    ELSE date(
                        date(expirationDate, 'start of month', '-1 month'),
                        '+' || (CAST(strftime('%d', expirationDate) AS INTEGER) - 1) || ' days'
                    )
                END
            """
        )
    }
}

private val Migration2To3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSql("ALTER TABLE cashback_rules ADD COLUMN bankCardId INTEGER NOT NULL DEFAULT 0")
        connection.execSql(
            """
            UPDATE cashback_rules
            SET bankCardId = COALESCE(
                (
                    SELECT cardId
                    FROM bank_cards
                    WHERE bank_cards.bankName = cashback_rules.bankCardName
                    ORDER BY cardId ASC
                    LIMIT 1
                ),
                0
            )
            """
        )
    }
}

val Migration3to4 = object : Migration(3, 4) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSql(
            """
            DELETE FROM cashback_rules
            WHERE cashbackRuleId NOT IN (
                SELECT MIN(cashbackRuleId)
                FROM cashback_rules
                GROUP BY bankCardId, category
            )
            """
        )
        connection.execSql(
            """
            DROP INDEX IF EXISTS `index_cashback_bankCardId_category`
            """
        )
        connection.execSql(
            """
            CREATE UNIQUE INDEX IF NOT EXISTS `index_cashback_bankCardId_category`
            ON cashback_rules (bankCardId, category)
            """
        )
    }
}

private fun SQLiteConnection.execSql(sql: String) {
    val statement = prepare(sql)
    try {
        statement.step()
    } finally {
        statement.close()
    }
}

@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

expect object CardCashbackDatabaseConstructor : RoomDatabaseConstructor<CardCashbackDatabase> {
    override fun initialize(): CardCashbackDatabase
}
