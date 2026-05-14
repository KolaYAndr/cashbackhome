package org.homesharing.cashbackhome.presentation.cards

import org.homesharing.cashbackhome.data.local.database.entity.BankCard
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule

internal sealed interface CardDetailsScreenState {
    data object Loading : CardDetailsScreenState

    data class Content(
        val card: BankCard,
        val cashbacks: List<CashbackRule>,
    ) : CardDetailsScreenState
}
