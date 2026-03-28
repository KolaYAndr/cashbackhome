package org.homesharing.cashbackhome.domain.model

data class CashbackRuleDraft(
    val cashbackRuleId: Long = 0,
    val percentage: Double = 0.0,
    val category: CashbackCategory = CashbackCategory.Other,
    val maxAmount: Double? = null,
    val expirationDate: String = ""
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

        companion object {
            val all: List<CashbackCategory> = listOf(
                AllPurchases,
                Pharmacies,
                CafesRestaurantsFastfood,
                ClothingAndShoes,
                GasStationsFuel,
                Taxi,
                Marketplaces,
                Transport,
                Supermarkets,
                TrainAndAirTickets,
                AutoServicesAndAutoGoods,
                HomeAndRepair,
                ChildrenGoods,
                CommunicationInternetTv,
                BeautyCosmetics,
                CultureEntertainment,
                Books,
                SouvenirsHobbies,
                FlowersAndGifts,
                Jewelry,
                TechnologyElectronics,
                SportsActiveLeisure,
                Utilities,
                DutyFree,
                Groceries,
                Cafe,
                Restaurant,
                Travel,
                OnlineShopping,
                Flowers,
                Pharmacy,
                Other,
            )
        }
    }
}
