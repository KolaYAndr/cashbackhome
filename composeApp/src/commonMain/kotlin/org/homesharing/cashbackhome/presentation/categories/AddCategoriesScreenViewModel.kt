package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.homesharing.cashbackhome.domain.usecase.UpsertCashbackRuleUseCase

internal class AddCategoriesScreenViewModel(
    private val upsertCashbackRuleUseCase: UpsertCashbackRuleUseCase
) : ViewModel() {
    fun upsertRule(rule: CashbackRuleDraft) {
        viewModelScope.launch {
            upsertCashbackRuleUseCase(rule)
        }
    }
}