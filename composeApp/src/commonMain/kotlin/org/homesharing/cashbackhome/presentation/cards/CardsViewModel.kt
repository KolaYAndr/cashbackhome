package org.homesharing.cashbackhome.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository

private const val THRESHOLD = 5_000L

internal class CardsViewModel(
    val applicationScope: CoroutineScope,
    val repository: CardCashbackRepository,
) : ViewModel() {
    val uiState = repository.getAllCards()
        .map { cards ->
            if (cards.isEmpty()) {
                CardsScreenState.EmptyScreen
            } else {
                CardsScreenState.Cards(cards)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(THRESHOLD),
            initialValue = CardsScreenState.Loading,
        )

    fun deleteCashbackRuleById(ruleId: Long) {
        applicationScope.launch {
            repository.deleteBankCardById(ruleId)
        }
    }
}
