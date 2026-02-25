package com.homesharing.cashbackhome.domain.repository

import com.homesharing.cashbackhome.domain.models.BankCard
import com.homesharing.cashbackhome.domain.models.BankCardWithCashback
import com.homesharing.cashbackhome.domain.models.CashbackRule
import kotlinx.coroutines.flow.Flow

interface CardCashbackRepository {

    // Aggregates
    fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>>
    fun getCardWithCashbacks(cardId: Long): Flow<BankCardWithCashback>

    // BankCard
    fun getAllCards(): Flow<List<BankCard>>
    suspend fun upsertBankCard(card: BankCard)
    suspend fun deleteBankCardById(cardId: Long)

    // CashbackRule
    fun getAllCashbackRules(): Flow<List<CashbackRule>>
    fun getCashbackRule(ruleId: Long): Flow<CashbackRule>
    suspend fun upsertCashbackRule(rule: CashbackRule)
    suspend fun deleteCashbackRuleById(ruleId: Long)

    // Junction
    suspend fun linkCardToRule(cardId: Long, ruleId: Long)
    suspend fun unlinkCardFromRule(cardId: Long, ruleId: Long)

    // Helpers / Filters
    fun searchCards(query: String): Flow<List<BankCardWithCashback>>
    fun getCardsByCategory(category: CashbackRule.CashbackCategory): Flow<List<BankCardWithCashback>>
}
