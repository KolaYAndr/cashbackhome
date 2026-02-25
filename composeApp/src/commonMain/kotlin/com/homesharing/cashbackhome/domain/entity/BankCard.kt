package com.homesharing.cashbackhome.domain.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bank_cards")
data class BankCard(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0,
    val mask: String,
    val bankName: String,
)