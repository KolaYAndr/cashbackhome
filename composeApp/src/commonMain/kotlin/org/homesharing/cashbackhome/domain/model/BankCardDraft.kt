package org.homesharing.cashbackhome.domain.model

data class BankCardDraft(
    val cardId: Long = 0,
    val bankName: String = "",
    val mask: String = "",
)