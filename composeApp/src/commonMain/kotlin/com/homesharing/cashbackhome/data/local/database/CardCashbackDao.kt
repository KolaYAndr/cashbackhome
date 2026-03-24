package com.homesharing.cashbackhome.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.homesharing.cashbackhome.data.local.database.entity.BankCard
import com.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import com.homesharing.cashbackhome.data.local.database.entity.CardCashback
import com.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import kotlinx.coroutines.flow.Flow

@Dao
interface CardCashbackDao {

    // --------- Aggregates (BankCard + Cashbacks) ---------

    @Transaction
    @Query("SELECT * FROM bank_cards")
    fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>>

    @Transaction
    @Query("SELECT * FROM bank_cards WHERE cardId = :cardId")
    fun getCardWithCashbacks(cardId: Long): Flow<BankCardWithCashback>


    // --------- BankCard ---------

    @Query("SELECT * FROM bank_cards")
    fun getAllCards(): Flow<List<BankCard>>

    @Upsert
    suspend fun upsertBankCard(card: BankCard)

    @Upsert
    suspend fun upsertBankCards(cards: List<BankCard>)

    @Delete
    suspend fun deleteBankCard(card: BankCard)

    @Query("DELETE FROM bank_cards WHERE cardId = :cardId")
    suspend fun deleteBankCardById(cardId: Long)


    // --------- CashbackRule ---------

    @Query("SELECT * FROM cashback_rules")
    fun getAllCashbackRules(): Flow<List<CashbackRule>>

    @Query("SELECT * FROM cashback_rules WHERE cashbackRuleId = :ruleId")
    fun getCashbackRule(ruleId: Long): Flow<CashbackRule>

    @Upsert
    suspend fun upsertCashbackRule(rule: CashbackRule)

    @Upsert
    suspend fun upsertCashbackRules(rules: List<CashbackRule>)

    @Delete
    suspend fun deleteCashbackRule(rule: CashbackRule)

    @Query("DELETE FROM cashback_rules WHERE cashbackRuleId = :ruleId")
    suspend fun deleteCashbackRuleById(ruleId: Long)


    // --------- Junction: CardCashback (many-to-many) ---------

    @Upsert
    suspend fun upsertCardCashback(junction: CardCashback)

    @Upsert
    suspend fun upsertCardCashbacks(junctions: List<CardCashback>)

    @Query("""
        DELETE FROM card_cashback_junction 
        WHERE cardId = :cardId AND cashbackRuleId = :ruleId
    """)
    suspend fun deleteCardCashback(cardId: Long, ruleId: Long)

    @Query("DELETE FROM card_cashback_junction WHERE cardId = :cardId")
    suspend fun deleteJunctionsByCardId(cardId: Long)

    @Query("DELETE FROM card_cashback_junction WHERE cashbackRuleId = :ruleId")
    suspend fun deleteJunctionsByRuleId(ruleId: Long)


    // --------- Helpers / Filters для UI ---------

    @Transaction
    @Query("""
        SELECT * FROM bank_cards
        WHERE bankName LIKE '%' || :query || '%'
           OR mask      LIKE '%' || :query || '%'
    """)
    fun searchCards(query: String): Flow<List<BankCardWithCashback>>

    @Transaction
    @Query("""
        SELECT DISTINCT bc.*
        FROM bank_cards AS bc
        INNER JOIN card_cashback_junction AS j 
            ON bc.cardId = j.cardId
        INNER JOIN cashback_rules AS cr 
            ON cr.cashbackRuleId = j.cashbackRuleId
        WHERE cr.category = :category
    """)
    fun getCardsByCategory(category: String): Flow<List<BankCardWithCashback>>
}