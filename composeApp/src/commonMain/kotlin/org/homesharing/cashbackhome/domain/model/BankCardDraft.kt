package org.homesharing.cashbackhome.domain.model

import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType

data class BankCardDraft(
    val cardId: Long = 0,
    val bankName: String = "",
    val title: String = "",
    val cardType: BankCardType = BankCardType.Debit,
)
