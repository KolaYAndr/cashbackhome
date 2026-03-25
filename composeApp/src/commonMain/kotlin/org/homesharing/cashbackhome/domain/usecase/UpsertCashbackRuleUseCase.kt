package org.homesharing.cashbackhome.domain.usecase

import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

class UpsertCashbackRuleUseCase(private val repository: CardCashbackRepository) {
    suspend operator fun invoke(rule: CashbackRuleDraft) = repository.upsertCashbackRule(rule)
}