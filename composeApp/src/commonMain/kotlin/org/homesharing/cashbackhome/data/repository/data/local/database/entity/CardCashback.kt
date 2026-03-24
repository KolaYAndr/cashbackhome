package org.homesharing.cashbackhome.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index

@Entity(
    primaryKeys = ["cardId", "cashbackRuleId"]   ,
    tableName = "card_cashback_junction",
    indices = [
        Index("cardId"),
        Index("cashbackRuleId")
    ]
)
data class CardCashback(
    val cardId: Long,
    val cashbackRuleId: Long,
)