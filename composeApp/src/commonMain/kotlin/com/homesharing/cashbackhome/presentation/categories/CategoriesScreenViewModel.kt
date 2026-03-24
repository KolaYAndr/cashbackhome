package com.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homesharing.cashbackhome.domain.usecase.GetAllCashbackRulesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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
