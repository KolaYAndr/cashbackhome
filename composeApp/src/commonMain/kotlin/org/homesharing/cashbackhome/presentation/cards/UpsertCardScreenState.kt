package org.homesharing.cashbackhome.presentation.cards

import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType

internal data class CardsForms(
    val cardId: Long = 0,
    val bankName: String? = null,
    val title: String? = null,
    val cardType: BankCardType = BankCardType.Debit,
    val isSaving: Boolean = false,
    val hasError: Boolean = false,
    val isSaved: Boolean = false,
)

internal sealed interface UpsertCardScreenState {
    data object Loading: UpsertCardScreenState

    data class UpsertCard(val forms: CardsForms, val isEditing: Boolean): UpsertCardScreenState
}

