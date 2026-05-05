package org.homesharing.cashbackhome.data.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import org.homesharing.cashbackhome.data.local.database.CardCashbackDao
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import org.homesharing.cashbackhome.data.local.database.entity.CardCashback
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import org.homesharing.cashbackhome.presentation.categories.SavedCategoryResult

internal class CardCashbackRepositoryImpl(
    private val applicationScope: CoroutineScope,
    private val dao: CardCashbackDao
) : CardCashbackRepository {

    // -------- Aggregates --------

    override fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>> =
        dao.getAllCardsWithCashbacks()

    override fun getCardWithCashbacks(cardId: Long): Flow<BankCardWithCashback> =
        dao.getCardWithCashbacks(cardId)


    // -------- BankCard --------

    override fun getAllCards(): Flow<List<BankCard>> = dao.getAllCards()

    override suspend fun getCard(cardId: Long) = dao.getCard(cardId)

    override suspend fun upsertBankCard(card: BankCard) {
        dao.upsertBankCard(card)
    }

    override suspend fun deleteBankCardById(cardId: Long) {
        // сначала чистим связи, затем карту
        dao.deleteJunctionsByCardId(cardId)
        dao.deleteBankCardById(cardId)
    }


    // -------- CashbackRule --------
    override fun getAllCashbackRules(): Flow<List<CashbackRule>> =
        dao.getAllCashbackRules()

    override fun getCashbackRule(ruleId: Long): Flow<CashbackRule> =
        dao.getCashbackRule(ruleId)

    override suspend fun upsertCashbackRule(rule: CashbackRule): SavedCategoryResult {
        return applicationScope.async<SavedCategoryResult> {
            try {
                val resultId = dao.upsertCashbackRule(rule)

                if (resultId == -1L) {
                    SavedCategoryResult.Duplicate
                } else {
                    SavedCategoryResult.Success(resultId)
                }
            }
            catch (_: Exception) {
                SavedCategoryResult.Error
            }
        }.await()
    }

    override suspend fun deleteCashbackRuleById(ruleId: Long) {
        dao.deleteJunctionsByRuleId(ruleId)
        dao.deleteCashbackRuleById(ruleId)
    }


    // -------- Junction --------

    override suspend fun linkCardToRule(cardId: Long, ruleId: Long) {
        dao.upsertCardCashback(
            CardCashback(
                cardId = cardId,
                cashbackRuleId = ruleId
            )
        )
    }

    override suspend fun unlinkCardFromRule(cardId: Long, ruleId: Long) {
        dao.deleteCardCashback(cardId, ruleId)
    }


    // -------- Helpers / Filters --------

    override fun searchCards(query: String): Flow<List<BankCardWithCashback>> =
        dao.searchCards(query)

    override fun getCardsByCategory(
        category: CashbackRule.CashbackCategory
    ): Flow<List<BankCardWithCashback>> =
        dao.getCardsByCategory(category.name)
}