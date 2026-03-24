package com.homesharing.cashbackhome.data.repository

import com.homesharing.cashbackhome.data.local.database.CardCashbackDao
import com.homesharing.cashbackhome.data.local.database.entity.BankCard
import com.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import com.homesharing.cashbackhome.data.local.database.entity.CardCashback
import com.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import com.homesharing.cashbackhome.data.mapper.DbEntityModelMapper
import com.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import com.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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
    private var cashbackList = mutableListOf<CashbackRuleDraft>()

    override fun getAllCashbackRules(): Flow<List<CashbackRuleDraft>> = flow {
        dao.getAllCashbackRules()
            .map {
                DbEntityModelMapper.cashbackRuleToCashBackRuleDraftList(it)
            }
            .collect {
                cashbackList = it.toMutableList()
                emit(
                    List(10) {
                        CashbackRuleDraft(
                            cashbackRuleId = it.toLong(),
                            title = "$it position",
                            percentage = it.toDouble() / 100,
                            category = CashbackRule.CashbackCategory.Cafe,
                            expirationDate = "2026-15-04"
                        )
                    }
                )
            }
    }

    override fun getCashbackRule(ruleId: Long): Flow<CashbackRuleDraft> =
        dao.getCashbackRule(ruleId).map {
            DbEntityModelMapper.cashbackRuleToCashBackRuleDraft(it)
        }

    override suspend fun upsertCashbackRule(rule: CashbackRuleDraft) {
        dao.upsertCashbackRule(
            DbEntityModelMapper.cashbackRuleDraftToCashBackRule(rule)
        )
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
