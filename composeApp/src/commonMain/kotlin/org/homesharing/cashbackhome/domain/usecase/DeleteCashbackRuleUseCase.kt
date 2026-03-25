package org.homesharing.cashbackhome.domain.usecase

import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

class DeleteCashbackRuleUseCase(private val repository: CardCashbackRepository) {
    suspend operator fun invoke(id: Long) = repository.deleteCashbackRuleById(id)
}