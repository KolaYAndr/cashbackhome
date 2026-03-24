package org.homesharing.cashbackhome.presentation.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.homesharing.cashbackhome.data.local.database.entity.BankCardWithCashback
import org.homesharing.cashbackhome.domain.repository.CardCashbackRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val THRESHOLD = 5000L

internal class CardsViewModel(
    repository: CardCashbackRepository
) : ViewModel() {

    val uiState: StateFlow<List<BankCardWithCashback>?> = repository.getAllCardsWithCashbacks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(THRESHOLD),
        initialValue = null
    )
}
