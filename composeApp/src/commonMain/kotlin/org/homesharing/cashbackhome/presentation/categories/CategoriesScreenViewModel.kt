package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.domain.usecase.DeleteCashbackRuleUseCase
import org.homesharing.cashbackhome.domain.usecase.GetAllCashbackRulesUseCase

internal class CategoriesScreenViewModel(
    private val getAllCashbackRulesUseCase: GetAllCashbackRulesUseCase,
    private val deleteCashbackRuleUseCase: DeleteCashbackRuleUseCase,
) : ViewModel() {
    val uiState = getAllCashbackRulesUseCase()
        .map {
            if (it.isEmpty()) {
                CategoriesScreenState.EmptyScreen
            } else {
                CategoriesScreenState.Categories(it)
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            CategoriesScreenState.EmptyScreen
        )

    fun deleteCashBackRuleById(ruleId: Long) {
        viewModelScope.launch {
            deleteCashbackRuleUseCase(ruleId)
        }
    }
}
