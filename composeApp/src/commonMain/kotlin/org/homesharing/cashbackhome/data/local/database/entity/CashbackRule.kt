package org.homesharing.cashbackhome.data.local.database.entity

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
        data object AllPurchases : CashbackCategory("all_purchases")
        data object Pharmacies : CashbackCategory("pharmacies")
        data object CafesRestaurantsFastfood : CashbackCategory("cafes_restaurants_fastfood")
        data object ClothingAndShoes : CashbackCategory("clothing_and_shoes")
        data object GasStationsFuel : CashbackCategory("gas_stations_fuel")
        data object Taxi : CashbackCategory("taxi")
        data object Marketplaces : CashbackCategory("marketplaces")
        data object Transport : CashbackCategory("transport")
        data object Supermarkets : CashbackCategory("supermarkets")
        data object TrainAndAirTickets : CashbackCategory("train_and_air_tickets")
        data object AutoServicesAndAutoGoods : CashbackCategory("auto_services_and_auto_goods")
        data object HomeAndRepair : CashbackCategory("home_and_repair")
        data object ChildrenGoods : CashbackCategory("children_goods")
        data object CommunicationInternetTv : CashbackCategory("communication_internet_tv")
        data object BeautyCosmetics : CashbackCategory("beauty_cosmetics")
        data object CultureEntertainment : CashbackCategory("culture_entertainment")
        data object Books : CashbackCategory("books")
        data object SouvenirsHobbies : CashbackCategory("souvenirs_hobbies")
        data object FlowersAndGifts : CashbackCategory("flowers_and_gifts")
        data object Jewelry : CashbackCategory("jewelry")
        data object TechnologyElectronics : CashbackCategory("technology_electronics")
        data object SportsActiveLeisure : CashbackCategory("sports_active_leisure")
        data object Utilities : CashbackCategory("utilities")
        data object DutyFree : CashbackCategory("duty_free")
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
        "all_purchases" -> CashbackRule.CashbackCategory.AllPurchases
        "pharmacies" -> CashbackRule.CashbackCategory.Pharmacies
        "cafes_restaurants_fastfood" -> CashbackRule.CashbackCategory.CafesRestaurantsFastfood
        "clothing_and_shoes" -> CashbackRule.CashbackCategory.ClothingAndShoes
        "gas_stations_fuel" -> CashbackRule.CashbackCategory.GasStationsFuel
        "taxi" -> CashbackRule.CashbackCategory.Taxi
        "marketplaces" -> CashbackRule.CashbackCategory.Marketplaces
        "transport" -> CashbackRule.CashbackCategory.Transport
        "supermarkets" -> CashbackRule.CashbackCategory.Supermarkets
        "train_and_air_tickets" -> CashbackRule.CashbackCategory.TrainAndAirTickets
        "auto_services_and_auto_goods" -> CashbackRule.CashbackCategory.AutoServicesAndAutoGoods
        "home_and_repair" -> CashbackRule.CashbackCategory.HomeAndRepair
        "children_goods" -> CashbackRule.CashbackCategory.ChildrenGoods
        "communication_internet_tv" -> CashbackRule.CashbackCategory.CommunicationInternetTv
        "beauty_cosmetics" -> CashbackRule.CashbackCategory.BeautyCosmetics
        "culture_entertainment" -> CashbackRule.CashbackCategory.CultureEntertainment
        "books" -> CashbackRule.CashbackCategory.Books
        "souvenirs_hobbies" -> CashbackRule.CashbackCategory.SouvenirsHobbies
        "flowers_and_gifts" -> CashbackRule.CashbackCategory.FlowersAndGifts
        "jewelry" -> CashbackRule.CashbackCategory.Jewelry
        "technology_electronics" -> CashbackRule.CashbackCategory.TechnologyElectronics
        "sports_active_leisure" -> CashbackRule.CashbackCategory.SportsActiveLeisure
        "utilities" -> CashbackRule.CashbackCategory.Utilities
        "duty_free" -> CashbackRule.CashbackCategory.DutyFree
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
