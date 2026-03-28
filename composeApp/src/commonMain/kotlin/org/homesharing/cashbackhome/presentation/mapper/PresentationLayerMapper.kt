package org.homesharing.cashbackhome.presentation.mapper

import androidx.compose.runtime.Composable
import cashbackhome.composeapp.generated.resources.Res
import cashbackhome.composeapp.generated.resources.category_cafe
import cashbackhome.composeapp.generated.resources.category_flowers
import cashbackhome.composeapp.generated.resources.category_groceries
import cashbackhome.composeapp.generated.resources.category_online_shopping
import cashbackhome.composeapp.generated.resources.category_other
import cashbackhome.composeapp.generated.resources.category_pharmacy
import cashbackhome.composeapp.generated.resources.category_restaurant
import cashbackhome.composeapp.generated.resources.category_travel
import cashbackhome.composeapp.generated.resources.category_type_all_purchases
import cashbackhome.composeapp.generated.resources.category_type_auto_services_goods
import cashbackhome.composeapp.generated.resources.category_type_beauty_cosmetics
import cashbackhome.composeapp.generated.resources.category_type_books
import cashbackhome.composeapp.generated.resources.category_type_cafes_restaurants_fastfood
import cashbackhome.composeapp.generated.resources.category_type_children_goods
import cashbackhome.composeapp.generated.resources.category_type_clothes_shoes
import cashbackhome.composeapp.generated.resources.category_type_communication_internet_tv
import cashbackhome.composeapp.generated.resources.category_type_culture_entertainment
import cashbackhome.composeapp.generated.resources.category_type_duty_free
import cashbackhome.composeapp.generated.resources.category_type_flowers_gifts
import cashbackhome.composeapp.generated.resources.category_type_gas_stations_fuel
import cashbackhome.composeapp.generated.resources.category_type_home_repair
import cashbackhome.composeapp.generated.resources.category_type_jewelry
import cashbackhome.composeapp.generated.resources.category_type_marketplaces
import cashbackhome.composeapp.generated.resources.category_type_pharmacies
import cashbackhome.composeapp.generated.resources.category_type_souvenirs_hobbies
import cashbackhome.composeapp.generated.resources.category_type_sports_outdoor
import cashbackhome.composeapp.generated.resources.category_type_supermarkets
import cashbackhome.composeapp.generated.resources.category_type_taxi
import cashbackhome.composeapp.generated.resources.category_type_technology_electronics
import cashbackhome.composeapp.generated.resources.category_type_train_air_tickets
import cashbackhome.composeapp.generated.resources.category_type_transport
import cashbackhome.composeapp.generated.resources.category_type_utilities
import org.homesharing.cashbackhome.domain.model.CashbackRuleDraft
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun categoryName(category: CashbackRuleDraft.CashbackCategory?): String {
    if (category == null)
        return ""
    return when (category) {
        CashbackRuleDraft.CashbackCategory.AllPurchases -> stringResource(Res.string.category_type_all_purchases)
        CashbackRuleDraft.CashbackCategory.Pharmacies -> stringResource(Res.string.category_type_pharmacies)
        CashbackRuleDraft.CashbackCategory.CafesRestaurantsFastfood -> stringResource(Res.string.category_type_cafes_restaurants_fastfood)
        CashbackRuleDraft.CashbackCategory.ClothingAndShoes -> stringResource(Res.string.category_type_clothes_shoes)
        CashbackRuleDraft.CashbackCategory.GasStationsFuel -> stringResource(Res.string.category_type_gas_stations_fuel)
        CashbackRuleDraft.CashbackCategory.Taxi -> stringResource(Res.string.category_type_taxi)
        CashbackRuleDraft.CashbackCategory.Marketplaces -> stringResource(Res.string.category_type_marketplaces)
        CashbackRuleDraft.CashbackCategory.Transport -> stringResource(Res.string.category_type_transport)
        CashbackRuleDraft.CashbackCategory.Supermarkets -> stringResource(Res.string.category_type_supermarkets)
        CashbackRuleDraft.CashbackCategory.TrainAndAirTickets -> stringResource(Res.string.category_type_train_air_tickets)
        CashbackRuleDraft.CashbackCategory.AutoServicesAndAutoGoods -> stringResource(Res.string.category_type_auto_services_goods)
        CashbackRuleDraft.CashbackCategory.HomeAndRepair -> stringResource(Res.string.category_type_home_repair)
        CashbackRuleDraft.CashbackCategory.ChildrenGoods -> stringResource(Res.string.category_type_children_goods)
        CashbackRuleDraft.CashbackCategory.CommunicationInternetTv -> stringResource(Res.string.category_type_communication_internet_tv)
        CashbackRuleDraft.CashbackCategory.BeautyCosmetics -> stringResource(Res.string.category_type_beauty_cosmetics)
        CashbackRuleDraft.CashbackCategory.CultureEntertainment -> stringResource(Res.string.category_type_culture_entertainment)
        CashbackRuleDraft.CashbackCategory.Books -> stringResource(Res.string.category_type_books)
        CashbackRuleDraft.CashbackCategory.SouvenirsHobbies -> stringResource(Res.string.category_type_souvenirs_hobbies)
        CashbackRuleDraft.CashbackCategory.FlowersAndGifts -> stringResource(Res.string.category_type_flowers_gifts)
        CashbackRuleDraft.CashbackCategory.Jewelry -> stringResource(Res.string.category_type_jewelry)
        CashbackRuleDraft.CashbackCategory.TechnologyElectronics -> stringResource(Res.string.category_type_technology_electronics)
        CashbackRuleDraft.CashbackCategory.SportsActiveLeisure -> stringResource(Res.string.category_type_sports_outdoor)
        CashbackRuleDraft.CashbackCategory.Utilities -> stringResource(Res.string.category_type_utilities)
        CashbackRuleDraft.CashbackCategory.DutyFree -> stringResource(Res.string.category_type_duty_free)
        CashbackRuleDraft.CashbackCategory.Groceries -> stringResource(Res.string.category_groceries)
        CashbackRuleDraft.CashbackCategory.Cafe -> stringResource(Res.string.category_cafe)
        CashbackRuleDraft.CashbackCategory.Restaurant -> stringResource(Res.string.category_restaurant)
        CashbackRuleDraft.CashbackCategory.Travel -> stringResource(Res.string.category_travel)
        CashbackRuleDraft.CashbackCategory.OnlineShopping -> stringResource(Res.string.category_online_shopping)
        CashbackRuleDraft.CashbackCategory.Flowers -> stringResource(Res.string.category_flowers)
        CashbackRuleDraft.CashbackCategory.Pharmacy -> stringResource(Res.string.category_pharmacy)
        CashbackRuleDraft.CashbackCategory.Other -> stringResource(Res.string.category_other)
    }
}