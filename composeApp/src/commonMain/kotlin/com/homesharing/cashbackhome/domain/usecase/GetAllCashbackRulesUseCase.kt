package com.homesharing.cashbackhome.domain.usecase

import com.homesharing.cashbackhome.domain.repository.CardCashbackRepository

class GetAllCashbackRulesUseCase(private val repository: CardCashbackRepository) {
    operator fun invoke() = repository.getAllCashbackRules()
}