package org.homesharing.cashbackhome.presentation.mapper

import androidx.compose.runtime.Composable
import cashbackhome.composeapp.generated.resources.Res
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
import cashbackhome.composeapp.generated.resources.category_type_other
import cashbackhome.composeapp.generated.resources.category_type_pharmacies
import cashbackhome.composeapp.generated.resources.category_type_souvenirs_hobbies
import cashbackhome.composeapp.generated.resources.category_type_sports_outdoor
import cashbackhome.composeapp.generated.resources.category_type_supermarkets
import cashbackhome.composeapp.generated.resources.category_type_taxi
import cashbackhome.composeapp.generated.resources.category_type_technology_electronics
import cashbackhome.composeapp.generated.resources.category_type_train_air_tickets
import cashbackhome.composeapp.generated.resources.category_type_transport
import cashbackhome.composeapp.generated.resources.category_type_utilities
import org.homesharing.cashbackhome.data.local.database.entity.CashbackRule
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun categoryName(category: CashbackRule.CashbackCategory?): String {
    if (category == null)
        return ""
    return when (category) {
        CashbackRule.CashbackCategory.AllPurchases -> stringResource(Res.string.category_type_all_purchases)
        CashbackRule.CashbackCategory.Pharmacy -> stringResource(Res.string.category_type_pharmacies)
        CashbackRule.CashbackCategory.CafesRestaurantsFastfood -> stringResource(Res.string.category_type_cafes_restaurants_fastfood)
        CashbackRule.CashbackCategory.ClothingAndShoes -> stringResource(Res.string.category_type_clothes_shoes)
        CashbackRule.CashbackCategory.GasStationsFuel -> stringResource(Res.string.category_type_gas_stations_fuel)
        CashbackRule.CashbackCategory.Taxi -> stringResource(Res.string.category_type_taxi)
        CashbackRule.CashbackCategory.Marketplaces -> stringResource(Res.string.category_type_marketplaces)
        CashbackRule.CashbackCategory.Transport -> stringResource(Res.string.category_type_transport)
        CashbackRule.CashbackCategory.Supermarkets -> stringResource(Res.string.category_type_supermarkets)
        CashbackRule.CashbackCategory.TrainAndAirTickets -> stringResource(Res.string.category_type_train_air_tickets)
        CashbackRule.CashbackCategory.AutoServicesAndAutoGoods -> stringResource(Res.string.category_type_auto_services_goods)
        CashbackRule.CashbackCategory.HomeAndRepair -> stringResource(Res.string.category_type_home_repair)
        CashbackRule.CashbackCategory.ChildrenGoods -> stringResource(Res.string.category_type_children_goods)
        CashbackRule.CashbackCategory.CommunicationInternetTv -> stringResource(Res.string.category_type_communication_internet_tv)
        CashbackRule.CashbackCategory.BeautyCosmetics -> stringResource(Res.string.category_type_beauty_cosmetics)
        CashbackRule.CashbackCategory.CultureEntertainment -> stringResource(Res.string.category_type_culture_entertainment)
        CashbackRule.CashbackCategory.Books -> stringResource(Res.string.category_type_books)
        CashbackRule.CashbackCategory.SouvenirsHobbies -> stringResource(Res.string.category_type_souvenirs_hobbies)
        CashbackRule.CashbackCategory.FlowersAndGifts -> stringResource(Res.string.category_type_flowers_gifts)
        CashbackRule.CashbackCategory.Jewelry -> stringResource(Res.string.category_type_jewelry)
        CashbackRule.CashbackCategory.TechnologyElectronics -> stringResource(Res.string.category_type_technology_electronics)
        CashbackRule.CashbackCategory.SportsActiveLeisure -> stringResource(Res.string.category_type_sports_outdoor)
        CashbackRule.CashbackCategory.Utilities -> stringResource(Res.string.category_type_utilities)
        CashbackRule.CashbackCategory.DutyFree -> stringResource(Res.string.category_type_duty_free)
        CashbackRule.CashbackCategory.Other -> stringResource(Res.string.category_type_other)
    }
}