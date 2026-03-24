package com.homesharing.cashbackhome.domain.repository

import com.homesharing.cashbackhome.data.local.database.entity.BankCard
import com.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import com.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import com.homesharing.cashbackhome.domain.model.CashbackRuleDraft
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
    fun getAllCashbackRules(): Flow<List<CashbackRuleDraft>>
    fun getCashbackRule(ruleId: Long): Flow<CashbackRuleDraft>
    suspend fun upsertCashbackRule(rule: CashbackRuleDraft)
    suspend fun deleteCashbackRuleById(ruleId: Long)

    // Junction
    suspend fun linkCardToRule(cardId: Long, ruleId: Long)
    suspend fun unlinkCardFromRule(cardId: Long, ruleId: Long)

    // Helpers / Filters
    fun searchCards(query: String): Flow<List<BankCardWithCashback>>
    fun getCardsByCategory(category: CashbackRule.CashbackCategory): Flow<List<BankCardWithCashback>>
}
