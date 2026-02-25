package com.homesharing.cashbackhome.data.repository

import com.homesharing.cashbackhome.data.local.CardCashbackDao
import com.homesharing.cashbackhome.domain.entity.BankCard
import com.homesharing.cashbackhome.domain.entity.BankCardWithCashback
import com.homesharing.cashbackhome.domain.entity.CardCashback
import com.homesharing.cashbackhome.domain.entity.CashbackRule
import com.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import kotlinx.coroutines.flow.Flow

class CardCashbackRepositoryImpl(
    private val dao: CardCashbackDao
) : CardCashbackRepository {

    // -------- Aggregates --------

    override fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>> =
        dao.getAllCardsWithCashbacks()

    override fun getCardWithCashbacks(cardId: Long): Flow<BankCardWithCashback> =
        dao.getCardWithCashbacks(cardId)


    // -------- BankCard --------

    override fun getAllCards(): Flow<List<BankCard>> = dao.getAllCards()

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

    override suspend fun upsertCashbackRule(rule: CashbackRule) {
        dao.upsertCashbackRule(rule)
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
