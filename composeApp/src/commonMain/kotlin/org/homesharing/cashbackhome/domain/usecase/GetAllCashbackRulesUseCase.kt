package org.homesharing.cashbackhome.domain.usecase

import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

class GetAllCashbackRulesUseCase(private val repository: CardCashbackRepository) {
    operator fun invoke() = repository.getAllCashbackRules()
}