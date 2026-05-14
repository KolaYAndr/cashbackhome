package org.homesharing.cashbackhome.presentation.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

internal class CategoriesScreenViewModel(
    private val applicationScope: CoroutineScope,
    private val repository: CardCashbackRepository
) : ViewModel() {
    val uiState = repository.getAllCashbackRules()
        .map {
            if (it.isEmpty()) {
                CategoriesScreenState.EmptyScreen
            } else {
                CategoriesScreenState.Categories(it)
            }
        }
        .onStart { emit(CategoriesScreenState.Loading) }
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            CategoriesScreenState.EmptyScreen
        )

    fun deleteCashbackRuleById(ruleId: Long) {
        applicationScope.launch {
            repository.deleteCashbackRuleById(ruleId)
        }
    }
}
