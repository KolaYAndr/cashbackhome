package org.homesharing.cashbackhome.presentation.cards

import org.homesharing.cashbackhome.data.local.database.entity.BankCard

internal sealed interface CardsScreenState {
    data object Loading : CardsScreenState
    data object EmptyScreen : CardsScreenState
    data class Cards(val cards: List<BankCard>) : CardsScreenState
}
