package org.homesharing.cashbackhome.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.homesharing.cashbackhome.data.local.database.CardCashbackDao
import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import org.homesharing.cashbackhome.data.local.database.entity.CardCashback
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.homesharing.cashbackhome.data.repository.data.mapper.DbEntityModelMapper
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

internal class CardCashbackRepositoryImpl(
    private val dao: CardCashbackDao,
    private val mapper: DbEntityModelMapper
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
    override fun getAllCashbackRules(): Flow<List<CashbackRuleDraft>> =
        dao.getAllCashbackRules()
            .map {
                it.map{ item -> mapper.cashbackRuleToCashBackRuleDraft(item) }
            }

    override fun getCashbackRule(ruleId: Long): Flow<CashbackRuleDraft> =
        dao.getCashbackRule(ruleId).map {
            mapper.cashbackRuleToCashBackRuleDraft(it)
        }

    override suspend fun upsertCashbackRule(rule: CashbackRuleDraft) {
        dao.upsertCashbackRule(
            mapper.cashbackRuleDraftToCashBackRule(rule)
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