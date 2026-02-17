package com.homesharing.cashbackhome.domain

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BankCardWithCashback(
    @Embedded
    val card: BankCard,
    @Relation(
        parentColumn = "cardId",
        entityColumn = "cashbackRuleId",
        associateBy = Junction(
            value = CardCashback::class,
            parentColumn = "cardId",
            entityColumn = "cashbackRuleId"
        )
    )
    val cashbacks: List<CashbackRule>,
)