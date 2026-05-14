package org.homesharing.cashbackhome.data.local.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import org.homesharing.cashbackhome.data.local.database.entity.BankCard.BankCardType

@Entity(tableName = "bank_cards")
@Serializable
data class BankCard(
    @PrimaryKey(autoGenerate = true)
    val cardId: Long = 0,
    val title: String,
    val bankName: String,
    val cardType: BankCardType = BankCardType.Debit,
) {
    @Serializable
    sealed class BankCardType(val name: String) {
        data object Credit: BankCardType("Credit")

        data object Debit: BankCardType("Debit")

        data object Other: BankCardType("Other")

        companion object {
            val all = listOf(
                Credit,
                Debit,
                Other,
            )
        }
    }
}

internal class BankCardTypeConverter {
    @TypeConverter
    fun fromCardType(cardType: BankCardType): String = cardType.name

    @TypeConverter
    fun toCardType(cardTypeName: String): BankCardType = when (cardTypeName) {
        "Credit" -> BankCardType.Credit
        "Debit" -> BankCardType.Debit
        else -> BankCardType.Debit
    }
}
