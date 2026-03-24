package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.homesharing.cashbackhome.domain.usecase.GetAllCashbackRulesUseCase

internal class CategoriesScreenViewModel(
    private val getAllCashbackRulesUseCase: GetAllCashbackRulesUseCase
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

    fun deleteCashBackRuleById() {

    }

    fun updateCashBackRuleById() {

    }
}
