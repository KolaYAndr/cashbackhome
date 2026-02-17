package com.homesharing.cashbackhome.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.homesharing.cashbackhome.domain.BankCardWithCashback
import com.homesharing.cashbackhome.domain.CardCashback
import kotlinx.coroutines.flow.Flow

@Dao
interface CardCashbackDao {
    @Transaction
    @Query("SELECT * FROM bank_cards")
    fun getAllCardsWithCashbacks(): Flow<List<BankCardWithCashback>>

    @Upsert
    suspend fun upsertCardCashback(junction: CardCashback)
}