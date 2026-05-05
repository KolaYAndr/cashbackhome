package org.homesharing.cashbackhome.domain.repository

import kotlinx.coroutines.flow.Flow
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.presentation.categories.SavedCategoryResult

internal interface CardCashbackRepository {

    // Aggregates
    fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>>
    fun getCardWithCashbacks(cardId: Long): Flow<BankCardWithCashback>

    // BankCard
    fun getAllCards(): Flow<List<BankCard>>
    suspend fun getCard(cardId: Long): BankCard
    suspend fun upsertBankCard(card: BankCard)
    suspend fun deleteBankCardById(cardId: Long)

    // CashbackRule
    fun getAllCashbackRules(): Flow<List<CashbackRule>>
    fun getCashbackRule(ruleId: Long): Flow<CashbackRule>
    suspend fun upsertCashbackRule(rule: CashbackRule): SavedCategoryResult
    suspend fun deleteCashbackRuleById(ruleId: Long)

    // Junction
    suspend fun linkCardToRule(cardId: Long, ruleId: Long)
    suspend fun unlinkCardFromRule(cardId: Long, ruleId: Long)

    // Helpers / Filters
    fun searchCards(query: String): Flow<List<BankCardWithCashback>>
    fun getCardsByCategory(category: CashbackRule.CashbackCategory): Flow<List<BankCardWithCashback>>
}