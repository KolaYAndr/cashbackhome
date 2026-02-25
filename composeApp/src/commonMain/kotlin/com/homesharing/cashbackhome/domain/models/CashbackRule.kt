package com.homesharing.cashbackhome.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "cashback_rules")
data class CashbackRule(
    @PrimaryKey(autoGenerate = true)
    val cashbackRuleId: Long = 0,
    val title: String,
    val percentage: Double,
    val category: CashbackCategory = CashbackCategory.Other,
    val maxAmount: Double?,
    val expirationDate: String,
) {
    sealed class CashbackCategory(val name: String) {
        data object Groceries : CashbackCategory("groceries")
        data object Cafe : CashbackCategory("cafe")
        data object Restaurant : CashbackCategory("restaurant")
        data object Travel : CashbackCategory("travel")
        data object OnlineShopping : CashbackCategory("online_shopping")
        data object Flowers : CashbackCategory("flowers")
        data object Pharmacy : CashbackCategory("pharmacy")
        data object Other : CashbackCategory("other")
    }
}

internal class CashbackCategoryConverter {
    @TypeConverter
    fun fromCategory(category: CashbackRule.CashbackCategory): String = category.name

    @TypeConverter
    fun toCategory(categoryName: String): CashbackRule.CashbackCategory = when (categoryName) {
        "groceries" -> CashbackRule.CashbackCategory.Groceries
        "cafe" -> CashbackRule.CashbackCategory.Cafe
        "restaurant" -> CashbackRule.CashbackCategory.Restaurant
        "online_shopping" -> CashbackRule.CashbackCategory.OnlineShopping
        "flowers" -> CashbackRule.CashbackCategory.Flowers
        "pharmacy" -> CashbackRule.CashbackCategory.Pharmacy
        "travel" -> CashbackRule.CashbackCategory.Travel
        else -> CashbackRule.CashbackCategory.Other
    }
}
